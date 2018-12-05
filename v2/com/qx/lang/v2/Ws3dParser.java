package com.qx.lang.v2;

import java.io.IOException;
import java.util.Stack;


/**
 * 
 * @author pc
 *
 *	handler -> setter
 *
 */
public class Ws3dParser {

	private Ws3dContext context;

	private Ws3dStreamReader reader;

	protected State state;

	private Stack<ParsingHandle> stack;

	private Setter setter;

	public Ws3dParser(Ws3dContext context, Ws3dStreamReader reader) {
		super();
		this.reader = reader;
		//rootBuilder = new RootParsedElement(context);
		
		state = readDeclaration;
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
		stack = new Stack<>();
		RootParsingHandle rootHandle = new RootParsingHandle();
		stack.push(rootHandle);
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
	private State readDeclaration = new State() {

		@Override
		public void parse() throws Ws3dParsingException, IOException {
			reader.readNext();

			String fieldName = reader.until(new char[]{':', '}', ']'}, null, null);
			System.out.println("[XML_Parser] read header: "+fieldName);

			char current = reader.getCurrentChar();
			if(current==':'){
				// acquire selected fieldHandler as new setter
				setter = stack.peek().getSetter(fieldName);

				switch(setter.getSort()){

				case PRIMITIVE:
					state = readPrimitive;	
					break;

				case PRIMITIVES_LIST:
					state = readPrimitivesList;
					break;

				case OBJECT:
					state = readObject;
					break;

				case OBJECTS_LIST:
					state = readObjectsList;
					break;
				}		
			}
			else if(current=='}' || current==']'){
				state = close;
			}
		}
	};


	private State readPrimitive = new State() {

		@Override
		public void parse() throws Ws3dParsingException, IOException {
			reader.readNext();
			String value = reader.until(new char[]{',', '}'}, null, null);
			try{
				((PrimitiveSetter) setter).set(value);
			} catch (Exception e) {
				throw new Ws3dParsingException(reader.line, reader.column, "Cannot set Object due to "+e.getMessage());
			}
			char c = reader.getCurrentChar();
			if(c==','){
				state = readDeclaration;
			}
			else if(c=='}' || c==']'){
				state = close;
			}
		}
	};

	private State readObject = new State() {

		@Override
		public void parse() throws Ws3dParsingException, IOException {

			// check type declaration start sequence
			reader.readNext();
			reader.check('(');
			
			reader.readNext();
			String typeName = reader.until(new char[]{')'}, null, new char[]{'}', '{', '-', '[', ']'});

			Ws3dTypeHandler typeHandler = context.get(typeName);
			if(typeHandler==null){
				throw new Ws3dParsingException("Unknow type: "+typeName);
			}
			ObjectParsingHandle handle = new ObjectParsingHandle(typeHandler);

			((ObjectSetter) setter).set(handle.object);
			stack.push(handle);

			// check type declaration start sequence
			reader.readNext();
			reader.check('{');

			state = readDeclaration;
		}
	};

	private State readPrimitivesList = new State() {

		@Override
		public void parse() throws Ws3dParsingException, IOException {

			PrimitivesListParsingHandle handle = new PrimitivesListParsingHandle((PrimitivesListSetter) setter);
			stack.push(handle);

			// check type declaration start sequence
			reader.readNext();
			reader.check('[');

			state = readDeclaration;
		}
	};

	private State readObjectsList = new State() {

		@Override
		public void parse() throws Ws3dParsingException, IOException {

			ObjectsListParsingHandle handle = new ObjectsListParsingHandle((ObjectsListSetter) setter);
			stack.push(handle);

			// check type declaration start sequence
			reader.readNext();
			reader.check('[');

			state = readDeclaration;
		}
	};






	private State close = new State() {

		@Override
		public void parse() throws Ws3dParsingException, IOException {
			char c = reader.getCurrentChar();
			if((stack.peek().isList() && c==']')||(!stack.peek().isList() && c=='}')){
				stack.pop().close();
				if(stack.size()>1){
					reader.readNext();
					if(reader.isOneOf(']', '}')){
						state = close; // keep closing
					}
					else if(reader.isCurrent(',')){
						state = readDeclaration;
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
