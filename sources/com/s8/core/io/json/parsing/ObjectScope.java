package com.s8.core.io.json.parsing;

import java.io.IOException;

import com.s8.core.io.json.JSON_Lexicon;
import com.s8.core.io.json.fields.FieldHandler;
import com.s8.core.io.json.types.TypeHandler;


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
		public boolean parse(Parser parser, StreamReader reader, boolean isVerbose) throws JSON_ParsingException, IOException {

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
			
			JSON_Lexicon context = parser.getContext();
			
			
			try {
				// handler
				TypeHandler handler = (def != null) ? context.get(def) : defaultType;
				
				if(handler == null) {
					throw new JSON_ParsingException("Cannot parse since no defined type");
				}
				
				// create object of this scope
				object = handler.createInstance();

				
				parser.pushScope(new PropsScope() {
					
					@Override
					public ParsingScope openProperty(String name) throws JSON_ParsingException {
						FieldHandler fieldHandler = handler.getFieldHandler(name);
						if(fieldHandler==null){
							throw new JSON_ParsingException("Unknown field: "+name);
						}
						return fieldHandler.openScope(object);
					}

					@Override
					public void onExhausted() throws JSON_ParsingException {
						// nothing to do
					}
				});
				
				/* will run closing when relaunched */
				state = new Closing();
				
				return false; // stop reading for this scope
			}
			catch (Exception e) {
				throw new JSON_ParsingException(reader.line, reader.column, e.getMessage());
			}
		}
	}
	
	
	
	private class Closing extends ParsingState {

		@Override
		public boolean parse(Parser parser, StreamReader reader, boolean isVerbose)
				throws JSON_ParsingException, IOException {

		
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


	public abstract void onParsed(Object object) throws JSON_ParsingException;
	

}
