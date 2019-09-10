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

	private Stack<ParsingHandle> stack;


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
	private class ReadDeclaration extends State {

		@Override
		public void parse() throws Ws3dParsingException, IOException {
			reader.readNext();

			String fieldName = reader.until(new char[]{':', '}', ']'}, null, null);
			System.out.println("[XML_Parser] read header: "+fieldName);

			char current = reader.getCurrentChar();
			if(current==':'){
				// acquire selected fieldHandler as new setter
				Setter setter = stack.peek().getSetter(fieldName);

				switch(setter.getSort()){

				case PRIMITIVE:
					state = new ReadPrimitive((PrimitiveSetter) setter);	
					break;

				case PRIMITIVES_ARRAY:
					state = new ReadPrimitivesList((PrimitivesListSetter) setter);
					break;

				case OBJECT:
					state = new ReadObject((ObjectSetter) setter);
					break;

				case OBJECTS_ARRAY:
					state = new ReadObjectsList((ObjectsArraySetter) setter);
					break;
				}		
			}
			else if(current=='}' || current==']'){
				state = close;
			}
		}
	};


	private class ReadPrimitive extends State {

		private PrimitiveSetter setter;

		public ReadPrimitive(PrimitiveSetter setter) {
			super();
			this.setter = setter;
		}

		@Override
		public void parse() throws Ws3dParsingException, IOException {
			reader.readNext();
			String value = reader.until(new char[]{',', '}'}, null, null);
			try{
				setter.set(value);
			} catch (Exception e) {
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

		private ObjectSetter setter;

		public ReadObject(ObjectSetter setter) {
			super();
			this.setter = setter;
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
			ObjectParsingHandle handle = new ObjectParsingHandle(typeHandler);

			setter.set(handle.object);
			stack.push(handle);

			// check type declaration start sequence
			reader.readNext();
			reader.check('{');

			state = new ReadDeclaration();
		}
	};

	private class ReadPrimitivesList extends State {

		private PrimitivesListSetter setter;

		public ReadPrimitivesList(PrimitivesListSetter setter) {
			super();
			this.setter = setter;
		}
		
		@Override
		public void parse() throws Ws3dParsingException, IOException {

			PrimitivesListParsingHandle handle = new PrimitivesListParsingHandle(setter);
			stack.push(handle);

			// check type declaration start sequence
			reader.readNext();
			reader.check('[');

			state = new ReadDeclaration();
		}
	};

	private class ReadObjectsList extends State {
		
		private ObjectsArraySetter setter;
		
		public ReadObjectsList(ObjectsArraySetter setter) {
			super();
			this.setter = setter;
		}

		@Override
		public void parse() throws Ws3dParsingException, IOException {

			ObjectsListParsingHandle handle = new ObjectsListParsingHandle(setter);
			stack.push(handle);

			// check type declaration start sequence
			reader.readNext();
			reader.check('[');

			state = new ReadDeclaration();
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
