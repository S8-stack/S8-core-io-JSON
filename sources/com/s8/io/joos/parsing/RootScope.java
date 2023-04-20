package com.s8.io.joos.parsing;

import java.io.IOException;


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

	public Object result;

	public RootScope() {
		super();
		state = new Opening2();
	}
	
	
	private class Opening2 extends ParsingState {

		@Override
		public boolean parse(Parser parser, StreamReader reader, boolean isVerbose)
				throws JOOS_ParsingException, IOException {
			
			// skip leading white spaces
			reader.skip('\n', '\t', ' ');


			reader.checkSequence("const ");
			reader.skip('\n', '\t', ' ');
			reader.checkSequence("ROOT");
			reader.skip('\n', '\t', ' ');
			reader.checkSequence("=");

			reader.moveNext();
			
			
			state = new Closing();
			
			parser.pushScope(new ObjectScope(null) {
				
				@Override
				public void onParsed(Object object) throws JOOS_ParsingException {
					result = object;
				}
			});
			
			return false;
		}
		
	}
	

	
	private class Closing extends ParsingState {

		@Override
		public boolean parse(Parser parser, StreamReader reader, boolean isVerbose)
				throws JOOS_ParsingException, IOException {
			
			// skip leading white spaces
			reader.skip('\n', '\t', ' ');


			reader.check(';');
		
			parser.popScope();
			
			return false;
		}
		
	}


	@Override
	public ScopeType getType() {
		return ScopeType.MAPPED;
	}

}
