package com.s8.io.joos.parsing;

import java.io.IOException;


/**
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 *
 */
public abstract class ListScope extends ParsingScope {

	@Override
	public ScopeType getType() {
		return ScopeType.LISTED;
	}
	
	public abstract ParsingScope openItemScope() throws JOOS_ParsingException;


	public ListScope() {
		super();
		state = new ReadHeader();		
	}




	public class ReadHeader extends State {

		@Override
		public boolean parse(Parser parser, StreamReader reader, boolean isVerbose) 
				throws JOOS_ParsingException, IOException {
			// check type declaration start sequence
			
			reader.check('[');

			state = new ReadItem(true);
			return true;
		}
	}


	public class ReadItem extends State {

		private boolean isFirst;
		
		public ReadItem(boolean isFirst) {
			super();
			this.isFirst = isFirst;
		}
		
		@Override
		public boolean parse(Parser parser, StreamReader reader, boolean isVerbose)
				throws JOOS_ParsingException, IOException {
			if(isFirst) {
				/* previous state reading stopped on '{', so move next */ 
				reader.check('[');
			}
			else {

				// close previous scope
				reader.moveNext();
				
				if(reader.is(',')) { reader.moveNext(); }
			}
			
			
			/* has something to open */
			if(reader.isOneOf('[', '{', '"', '\'') || reader.isAlphanumeric()){
				
				/* dive into sub-scope */
				parser.pushScope(openItemScope());
				
				/* setup state as read next entry for next time calling parse on this scope*/
				if(isFirst) {
					state = new ReadItem(false);	
				}

				/* stop reading*/
				return false;
			}
			else if(reader.is(']')){
				
			
				/* close the current scope */
				close();

				/* remove current scope from stack */
				parser.popScope();

				/* stop reading*/
				return false;
			}
			else {
				throw new JOOS_ParsingException(reader.line, reader.column, "Done");
			}
		}
	}


	/**
	 * close this scope
	 * @throws JOOS_ParsingException
	 */
	public abstract void close() throws JOOS_ParsingException;
}
