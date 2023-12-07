package com.s8.core.io.json.parsing;

import java.io.IOException;

import com.s8.core.io.json.types.TypeHandler;


/**
 * 
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 * 
 * @author pierreconvert
 *
 */
public class RootScope extends ParsingScope {
	
	public enum Style {
		JSON,
		JS;
	}

	public Object result;
	
	private TypeHandler rootTypeHandlerHint;
	
	private Style style;

	public RootScope(TypeHandler typeHandler) {
		super();
		state = new ConsumeOpening();
		this.rootTypeHandlerHint = typeHandler;
	}
	
	
	private class ConsumeOpening extends ParsingState {

		@Override
		public boolean parse(Parser parser, StreamReader reader, boolean isVerbose)
				throws JSON_ParsingException, IOException {
			
			// skip leading white spaces
			reader.skip('\n', '\t', ' ');


			if(reader.is('{')) {
				style = Style.JSON;
			}
			else {
				style = Style.JS;
				reader.checkSequence("const ");
				reader.skip('\n', '\t', ' ');
				reader.checkSequence("ROOT");
				reader.skip('\n', '\t', ' ');
				reader.checkSequence("=");

				reader.moveNext();
			}
			
			
			parser.pushScope(new ObjectScope(rootTypeHandlerHint) {
				
				@Override
				public void onParsed(Object object) throws JSON_ParsingException {
					result = object;
				}
			});
			
			state = new Closing();
			return false;
		}
		
	}
	

	
	private class Closing extends ParsingState {

		@Override
		public boolean parse(Parser parser, StreamReader reader, boolean isVerbose)
				throws JSON_ParsingException, IOException {
			
			// skip leading white spaces
			reader.skip('\n', '\t', ' ');

			if(style == Style.JS) {
				reader.check(';');
			}
		
			parser.popScope();
			
			return false;
		}
		
	}


	@Override
	public ScopeType getType() {
		return ScopeType.MAPPED;
	}

}
