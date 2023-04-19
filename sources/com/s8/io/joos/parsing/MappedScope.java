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
public abstract class MappedScope extends ParsingScope {

	@Override
	public ScopeType getType() {
		return ScopeType.MAPPED;
	}



	public abstract ParsingScope openEntry(String declarator) throws JOOS_ParsingException;
	

	public MappedScope() {
		super();
		state = new ReadEntry(true);
	}


	public class ReadEntry extends State {

		private boolean isFirst;

		public ReadEntry(boolean isFirst) {
			super();
			this.isFirst = isFirst;
		}

		@Override
		public boolean parse(Parser parser, StreamReader reader, boolean isVerbose)
				throws JOOS_ParsingException, IOException {
			
			/* previous state reading stopped on '{' (first item) or ',' (next items) */ 
			reader.check(isFirst ? '{' : ',');
				
			/* previous state reading stopped on '{', so move next (and skip white spaces, eol, tabs) */ 
			reader.moveNext(); 
			
			if(reader.isAlphanumeric()){
				String declarator = reader.readAlphanumericChain();
				
				reader.moveNext(); 
				reader.check(':');
				
				/* dive into sub-scope */
				parser.pushScope(openEntry(declarator));

				/* setup state as read next entry for next time calling parse on this scope*/
				if(isFirst) {
					state = new ReadEntry(false);	
				}

				/* stop reading*/
				return false;
				
			}
			else if(reader.is('}')){
				
				
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
