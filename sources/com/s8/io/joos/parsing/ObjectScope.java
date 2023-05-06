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


	/**
	 * Can be null
	 */
	public final TypeHandler defaultType;
	
	private Object object;

	private boolean isPolymorphic;


	public ObjectScope(TypeHandler defaultType) {
		super();
		this.defaultType = defaultType;
		state = new ReadOpening();
	}



	public Object getValue() {
		return object;
	}

	

	private class ReadOpening extends ParsingState {


		@Override
		public boolean parse(Parser parser, StreamReader reader, boolean isVerbose) throws JOOS_ParsingException, IOException {

			String def = null;

			reader.skip('\n', '\t', ' ');
			
			if(reader.is('{')) {
				isPolymorphic = false;
				def = null;
			}
			else if(reader.isAlphanumeric()){
				isPolymorphic = true;

				def = reader.readAlphanumericChain();
			
				reader.check('(');
				reader.moveToNextSymbol();
				reader.check('{');
			}
			
			JOOS_Lexicon context = parser.getContext();
			
			
			try {
				// handler
				TypeHandler handler = (def != null) ? context.get(def) : defaultType;
				
				if(handler == null) {
					throw new JOOS_ParsingException("Cannot parse since no defined type");
				}
				
				// create object of this scope
				object = handler.createInstance();

				
				parser.pushScope(new PropsScope() {
					
					@Override
					public ParsingScope openProperty(String name) throws JOOS_ParsingException {
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
	
	
	
	private class Closing extends ParsingState {

		@Override
		public boolean parse(Parser parser, StreamReader reader, boolean isVerbose)
				throws JOOS_ParsingException, IOException {

		
			if(isPolymorphic) {
				reader.skip('\n', '\t', ' ');
				reader.check(')');
				reader.moveNext();
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
