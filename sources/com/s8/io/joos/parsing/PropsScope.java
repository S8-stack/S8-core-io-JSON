package com.s8.io.joos.parsing;

import java.io.IOException;

/**
 * 
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 *
 */
public abstract class PropsScope extends ParsingScope {

	@Override
	public ScopeType getType() {
		return ScopeType.MAPPED;
	}



	public abstract ParsingScope openProperty(String declarator) throws JOOS_ParsingException;
	

	public PropsScope() {
		super();
		state = new ReadEntry(true);
	}


	public class ReadEntry extends ParsingState {

		private boolean isFirst;

		public ReadEntry(boolean isFirst) {
			super();
			this.isFirst = isFirst;
		}

		@Override
		public boolean parse(Parser parser, StreamReader reader, boolean isVerbose)
				throws JOOS_ParsingException, IOException {
			
			if(isFirst) {
				// skip any leading chars
				reader.skip('\n', '\t', ' ');
				
				/* previous state reading stopped on '{' (first item) or ',' (next items) */ 
				reader.check('{');
				
				reader.moveToNextSymbol();
			}
			else {
				reader.skip('\n', '\t', ' ');
				if(reader.is(',')) { 
					reader.moveToNextSymbol();
				}
			}
			
			if(reader.isAlphanumeric()){
				String declarator = reader.readAlphanumericChain();
				
				reader.skip('\n', '\t', ' ');
				reader.check(':');
				reader.moveNext();
				
				/* dive into sub-scope */
				parser.pushScope(openProperty(declarator));

				/* setup state as read next entry for next time calling parse on this scope*/
				if(isFirst) {
					state = new ReadEntry(false);	
				}

				/* stop reading*/
				return false;
				
			}
			else if(reader.is('}')){
				
				/* consumed scope closing char */
				reader.moveNext();
				
				onExhausted();

				/* remove current scope from stack */
				parser.popScope();

				/* stop reading*/
				return false;
			}
			else {
				throw new JOOS_ParsingException(reader.line, reader.column, "Unexpected char: "+reader.getCurrentChar());
			}
		}
	}
	
	
	
	public abstract void onExhausted() throws JOOS_ParsingException;

}
