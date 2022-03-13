package com.s8.io.joos.parsing;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import com.s8.io.joos.JOOS_Lexicon;


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

	@Override
	public boolean isClosedBy(char c) {
		return c=='}';
	}



	public abstract ParsingScope openEntry(String declarator) throws JOOS_ParsingException;


	public abstract boolean isDefinable();

	public abstract void define(String definition, JOOS_Lexicon context) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException;



	public MappedScope() {
		super();
		if(isDefinable()) {
			state = new ReadHeader();
		}
		else {
			state = new ReadOpening();
		}
	}


	/**
	 * 
	 * @author pc
	 *
	 */
	public class ReadHeader extends State {

		@Override
		public boolean parse(Parser parser, StreamReader reader, boolean isVerbose) 
				throws JOOS_ParsingException, IOException {
			// check type declaration start sequence
			reader.check('(');

			reader.readNext();
			String definition = reader.until(new char[]{')'}, null, new char[]{'}', '{', '-', '[', ']'});
			try {
				define(definition.strip(), parser.getContext());
			}
			catch (Exception e) {
				throw new JOOS_ParsingException(reader.line, reader.column, e.getMessage());
			}
			
			// consume ')'
			reader.readNext();
			reader.skipWhiteSpaces();
			
			state = new ReadOpening();
			return true;
		}
	}


	public class ReadOpening extends State {

		@Override
		public boolean parse(Parser parser, StreamReader reader, boolean isVerbose) throws JOOS_ParsingException, IOException {
			reader.check('{');
			state = new ReadEntry(true);
			return true;
		}
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
			if(isFirst) {
				/* previous state reading stopped on '{', so move next */ 
				reader.readNext();
				reader.skipWhiteSpaces();
			}
			/* previous scope reading stopped on one of the following chars: '}', ',' */ 
			char current = reader.getCurrentChar();
			
			if(current=='}'){

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
					reader.readNext();
					reader.skipWhiteSpaces();
				}
				
				String declarator = reader.until(new char[]{':', '}', ']'}, null, null);
				if(isVerbose) {
					System.out.println("[JOOS_Parser] read declarator: "+declarator);
				}

				/* only definition opener ':' allowed here */
				reader.check(':'); 
				/* consume opener */
				reader.readNext();
				reader.skipWhiteSpaces();
				
				/* dive into sub-scope */
				parser.pushScope(openEntry(declarator));
				
				/* setup state as read next entry for next time calling parse on this scope*/
				if(isFirst) {
					state = new ReadEntry(false);	
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
