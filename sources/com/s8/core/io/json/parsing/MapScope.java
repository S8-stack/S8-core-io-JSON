package com.s8.core.io.json.parsing;

import java.io.IOException;

/**
 * 
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 *
 */
public abstract class MapScope extends ParsingScope {

	@Override
	public ScopeType getType() {
		return ScopeType.MAPPED;
	}



	public abstract ParsingScope openEntry(String declarator) throws JSON_ParsingException;
	

	public MapScope() {
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
				throws JSON_ParsingException, IOException {
			
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
			
			if(reader.is('"')){
				
				reader.moveNext();
				
				StringBuilder keyBuilder = new StringBuilder();
				while(!reader.isOneOf('"', '\n')) {
					keyBuilder.append(reader.getCurrentChar());
					reader.moveNext();
				}
				
				/* consume scope closing char */
				reader.moveNext();
				
				String key = keyBuilder.toString();
				
				reader.skip('\n', '\t', ' ');
				reader.check(':');
				reader.moveNext();
				
				/* dive into sub-scope */
				parser.pushScope(openEntry(key));

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
				throw new JSON_ParsingException(reader.line, reader.column, "Unexpected char: "+reader.getCurrentChar());
			}
		}
	}
	
	
	
	public abstract void onExhausted() throws JSON_ParsingException;

}
