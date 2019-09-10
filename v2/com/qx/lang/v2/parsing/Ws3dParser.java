package com.qx.lang.v2.parsing;

import java.io.IOException;
import java.util.Stack;

import com.qx.lang.v2.Ws3dContext;
import com.qx.lang.v2.Ws3dParsingException;
import com.qx.lang.v2.type.TypeHandler;


/**
 * 
 * @author pc
 *
 *	handler -> setter
 *
 */
public class Ws3dParser {

	private Ws3dContext context;

	private StreamReader reader;

	protected State state;

	private Stack<ParsingScope> scopes;


	public Ws3dParser(Ws3dContext context, StreamReader reader) {
		super();
		this.reader = reader;
		//rootBuilder = new RootParsedElement(context);

		state = new ReadDeclaration();
		this.context = context;
	}



	/**
	 * 
	 * @return
	 * @throws XML_ParsingException 
	 * @throws IOException 
	 * @throws Exception
	 */
	public Object parse() throws Ws3dParsingException, IOException {
		scopes = new Stack<>();
		RootScope rootHandle = new RootScope();
		scopes.push(rootHandle);
		while(state!=null){
			try {
				state.parse();
			}
			catch (Ws3dParsingException e) {
				e.acquire(reader);
				throw e;
			}
		}
		return rootHandle.result;
	}



	/**
	 * 
	 * @author pc
	 *
	 */
	private abstract class State {

		public abstract void parse() throws Ws3dParsingException, IOException;

	}






	/**
	 * read doc header
	 */
	private class ReadDeclaration extends State {

		@Override
		public void parse() throws Ws3dParsingException, IOException {
			reader.readNext();

			String declarator = reader.until(new char[]{':', '}', ']'}, null, null);
			System.out.println("[XML_Parser] read header: "+declarator);

			char current = reader.getCurrentChar();
			if(current==':'){
				// acquire selected fieldHandler as new setter
				ParsingScope scope = scopes.peek().enter(declarator);

				switch(scope.getType()){

				case PRIMITIVE:
					state = new ReadPrimitive((PrimitiveScope) scope);	
					break;

				case PRIMITIVES_ARRAY:
					state = new ReadPrimitivesList((PrimitivesArrayScope) scope);
					break;

				case OBJECT:
					state = new ReadObject((ObjectScope) scope);
					break;

				case OBJECTS_ARRAY:
					state = new ReadObjectsList((ObjectsArrayScope) scope);
					break;
				}		
			}
			else if(current=='}' || current==']'){
				state = close;
			}
		}
	};


	private class ReadPrimitive extends State {

		private PrimitiveScope scope;

		public ReadPrimitive(PrimitiveScope scope) {
			super();
			this.scope = scope;
		}

		@Override
		public void parse() throws Ws3dParsingException, IOException {
			reader.readNext();
			String value = reader.until(new char[]{',', '}'}, null, null);
			try{
				scope.define(value);
			} 
			catch (Exception e) {
				throw new Ws3dParsingException(reader.line, reader.column, "Cannot set Object due to "+e.getMessage());
			}
			char c = reader.getCurrentChar();
			if(c==','){
				state = new ReadDeclaration();
			}
			else if(c=='}' || c==']'){
				state = close;
			}
		}
	};

	private class ReadObject extends State {

		private ObjectScope scope;

		public ReadObject(ObjectScope scope) {
			super();
			this.scope = scope;
		}

		@Override
		public void parse() throws Ws3dParsingException, IOException {

			// check type declaration start sequence
			reader.readNext();
			reader.check('(');

			reader.readNext();
			String typeName = reader.until(new char[]{')'}, null, new char[]{'}', '{', '-', '[', ']'});

			TypeHandler typeHandler = context.get(typeName);
			if(typeHandler==null){
				throw new Ws3dParsingException("Unknow type: "+typeName);
			}

			// check type declaration start sequence
			reader.readNext();
			reader.check('{');

			scope.define(typeHandler);

			scopes.push(scope);

			state = new ReadDeclaration();
		}
	};

	private class ReadPrimitivesList extends State {

		private PrimitivesArrayScope scope;

		public ReadPrimitivesList(PrimitivesArrayScope scope) {
			super();
			this.scope = scope;
		}

		@Override
		public void parse() throws Ws3dParsingException, IOException {

			scopes.push(scope);

			// check type declaration start sequence
			reader.readNext();
			reader.check('(');

			reader.readNext();
			String lengthDeclaration = reader.until(new char[]{')'}, null, new char[]{'}', '{', '-', '[', ']'});

			int length = Integer.valueOf(lengthDeclaration);

			// check type declaration start sequence
			reader.readNext();
			reader.check('[');

			scope.define(length);

			scopes.push(scope);


			state = new ReadDeclaration();
		}
	};

	private class ReadObjectsList extends State {

		private ObjectsArrayScope scope;

		public ReadObjectsList(ObjectsArrayScope scope) {
			super();
			this.scope = scope;
		}

		@Override
		public void parse() throws Ws3dParsingException, IOException {

			// check type declaration start sequence
			reader.readNext();
			reader.check('(');

			reader.readNext();
			String lengthDeclaration = reader.until(new char[]{')'}, null, new char[]{'}', '{', '-', '[', ']'});

			int length = Integer.valueOf(lengthDeclaration);

			// check type declaration start sequence
			reader.readNext();
			reader.check('[');

			scope.define(length);

			scopes.push(scope);

			state = new ReadDeclaration();
		}
	};




	private State close = new State() {

		@Override
		public void parse() throws Ws3dParsingException, IOException {
			char c = reader.getCurrentChar();
			if(scopes.peek().isClosedBy(c)){
				scopes.pop();
				if(scopes.size()>1){
					reader.readNext();
					if(reader.isOneOf(']', '}')){
						state = close; // keep closing
					}
					else if(reader.isCurrent(',')){
						state = new ReadDeclaration();
					}
					else{
						throw new Ws3dParsingException("Illegal character");
					}	
				}
				else{
					state = null; // end of file
				}
			}
			else{
				throw new Ws3dParsingException("Illegal termination");
			}
		}
	};
}
