package com.s8.io.joos.parsing;

import java.io.IOException;


/**
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 *
 */
public abstract class ListedScope extends ParsingScope {

	@Override
	public ScopeType getType() {
		return ScopeType.LISTED;
	}
	
	public abstract ParsingScope openItem() throws JOOS_ParsingException;

	public abstract boolean isDefinable();

	public abstract void define(String definition);


	public ListedScope() {
		super();
		if(isDefinable()) {
			state = new ReadHeader();
		}
		else {
			state = new ReadListOpening();
		}
	}




	public class ReadHeader extends State {

		@Override
		public boolean parse(Parser parser, StreamReader reader, boolean isVerbose) 
				throws JOOS_ParsingException, IOException {
			// check type declaration start sequence
			
			reader.check('(');

			reader.readNext();
			String definition = reader.until(new char[]{')'}, null, new char[]{'}', '{', '-', '[', ']'});
			try {
				define(definition.strip());	
			}
			catch (Exception e) {
				throw new JOOS_ParsingException(reader.line, reader.column, e.getMessage());
			}
			
			// consume ')'
			reader.readNext();
			reader.skipWhiteSpaces();
			
			state = new ReadListOpening();
			return true;
		}
	}


	public class ReadListOpening extends State {

		@Override
		public boolean parse(Parser parser, StreamReader reader, boolean isVerbose) throws JOOS_ParsingException, IOException {
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
				reader.readNext();
				reader.skipWhiteSpaces();
			}
			/* previous scope reading stopped on one of the following chars: '}', ',' */ 
			char current = reader.getCurrentChar();
			
			if(current==']'){
				
				// consumer closer
				reader.readNext();
				reader.skipWhiteSpaces();

				/* close the current scope */
				close();

				/* remove current scope from stack */
				parser.popScope();

				/* stop reading*/
				return false;
			}
			else {
				if(!isFirst) {
					/* column is mandatory for next one */
					reader.check(',');
					
					// consumer opener
					reader.readNext();
					reader.skipWhiteSpaces();
				}

				/* dive into sub-scope */
				parser.pushScope(openItem());
				
				/* setup state as read next entry for next time calling parse on this scope*/
				if(isFirst) {
					state = new ReadItem(false);	
				}

				/* stop reading*/
				return false;
			}
		}
	}


	/**
	 * close this scope
	 * @throws JOOS_ParsingException
	 */
	public abstract void close() throws JOOS_ParsingException;
}
