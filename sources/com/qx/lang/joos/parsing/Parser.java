package com.qx.lang.joos.parsing;

import java.io.IOException;
import java.util.Stack;

import com.qx.lang.joos.JOOS_Context;
import com.qx.lang.joos.JOOS_ParsingException;
import com.qx.lang.joos.type.TypeHandler;


/**
 * 
 * @author pc
 *
 *	handler -> setter
 *
 */
public class Parser {

	private boolean isVerbose;

	private JOOS_Context context;

	private StreamReader reader;

	protected State state;

	private Stack<ParsingScope> scopes;


	public Parser(JOOS_Context context, StreamReader reader, boolean isVerbose) {
		super();
		this.reader = reader;
		//rootBuilder = new RootParsedElement(context);

		state = new ReadDeclaration();
		this.isVerbose = isVerbose;
		this.context = context;
	}



	/**
	 * 
	 * @return
	 * @throws XML_ParsingException 
	 * @throws IOException 
	 * @throws Exception
	 */
	public Object parse() throws JOOS_ParsingException, IOException {
		scopes = new Stack<>();
		RootScope rootHandle = new RootScope();
		scopes.push(rootHandle);
		while(state!=null){
			try {
				state.parse();
			}
			catch (JOOS_ParsingException e) {
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

		public abstract void parse() throws JOOS_ParsingException, IOException;

	}






	/**
	 * read doc header
	 */
	private class ReadDeclaration extends State {

		@Override
		public void parse() throws JOOS_ParsingException, IOException {
			reader.readNext();

			String declarator = reader.until(new char[]{':', '}', ']'}, null, null);
			if(isVerbose) {
				System.out.println("[JOOS_Parser] read header: "+declarator);
			}
			
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
		public void parse() throws JOOS_ParsingException, IOException {
			reader.readNext();

			String value = null;
			if(reader.is('(')) {
				reader.readNext();
				String declarator = reader.until(new char[]{')'}, null, new char[]{'}', '{', '-', '[', ']'});
				int length = Integer.valueOf(declarator);
				value = reader.readBlock(length);

				reader.readNext();
			}
			else {
				value = reader.until(new char[]{',', '}', ']'}, null, null);	
			}

			try{
				scope.define(value);
			}
			catch (Exception e) {
				throw new JOOS_ParsingException(reader.line, reader.column, "Cannot set Object due to "+e.getMessage());
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
		public void parse() throws JOOS_ParsingException, IOException {

			// check type declaration start sequence
			reader.readNext();
			reader.check('(');

			reader.readNext();
			String typeName = reader.until(new char[]{')'}, null, new char[]{'}', '{', '-', '[', ']'});

			TypeHandler typeHandler = context.get(typeName);
			if(typeHandler==null){
				throw new JOOS_ParsingException("Unknow type: "+typeName);
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
		public void parse() throws JOOS_ParsingException, IOException {

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
		public void parse() throws JOOS_ParsingException, IOException {

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
		public void parse() throws JOOS_ParsingException, IOException {
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
						throw new JOOS_ParsingException("Illegal character");
					}	
				}
				else{
					state = null; // end of file
				}
			}
			else{
				throw new JOOS_ParsingException("Illegal termination: "+c);
			}
		}
	};
}
