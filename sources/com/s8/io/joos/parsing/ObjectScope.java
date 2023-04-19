package com.s8.io.joos.parsing;

import java.io.IOException;

import com.s8.io.joos.JOOS_Lexicon;
import com.s8.io.joos.fields.FieldHandler;
import com.s8.io.joos.types.TypeHandler;


/**
 * 
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 *
 */
public abstract class ObjectScope extends ParsingScope {

	@Override
	public ScopeType getType() {
		return ScopeType.OBJECT;
	}


	public final TypeHandler defaultType;
	
	private Object object;

	private boolean isPolymorphic;


	public ObjectScope(TypeHandler defaultType) {
		super();
		this.defaultType = defaultType;
	}



	public Object getValue() {
		return object;
	}

	

	public class ReadOpening extends State {


		@Override
		public boolean parse(Parser parser, StreamReader reader, boolean isVerbose) throws JOOS_ParsingException, IOException {

			String def = null;

			reader.moveNext();
			if(reader.is('{')) {
				isPolymorphic = false;
				def = null;
			}
			else if(reader.isAlphanumeric()){
				isPolymorphic = true;

				def = reader.readAlphanumericChain();
			
				reader.check('(');
				reader.moveNext();
				reader.check('{');
			}
			
			JOOS_Lexicon context = parser.getContext();
			
			
			try {
				// handler
				TypeHandler handler = (def != null) ? context.get(def) : defaultType;
				
				// create object of this scope
				object = handler.createInstance();

				
				parser.pushScope(new MappedScope() {
					
					@Override
					public ParsingScope openEntry(String name) throws JOOS_ParsingException {
						FieldHandler fieldHandler = handler.getFieldHandler(name);
						if(fieldHandler==null){
							throw new JOOS_ParsingException("Unknown field: "+name);
						}
						return fieldHandler.openScope(object);
					}

					@Override
					public void onExhausted() throws JOOS_ParsingException {
						// nothing to do
					}
				});
				
				/* will run closing when relaunched */
				state = new Closing();
				
				return false; // stop reading for this scope
			}
			catch (Exception e) {
				throw new JOOS_ParsingException(reader.line, reader.column, e.getMessage());
			}
		}
	}
	
	
	
	public class Closing extends State {

		@Override
		public boolean parse(Parser parser, StreamReader reader, boolean isVerbose)
				throws JOOS_ParsingException, IOException {

			reader.check('}');
			
			if(isPolymorphic) {
				reader.moveNext();
				reader.check(')');
			}
			
			// object if now parsed
			onParsed(object);
			
			/* done with this scope */
			parser.popScope();
			
			return false; // stop reading
		}
	}


	public abstract void onParsed(Object object) throws JOOS_ParsingException;
	

}
