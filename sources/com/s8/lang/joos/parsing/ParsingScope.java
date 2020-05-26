package com.s8.lang.joos.parsing;

import java.io.IOException;

import com.s8.lang.joos.JOOS_ParsingException;
import com.s8.lang.joos.ParsingException;

public abstract class ParsingScope {
	
	public enum ScopeType {
		/** when in map-type scope, expected declarator before all definition */
		MAPPED, 
		
		/** direct list (no declarator) */
		LISTED,
		
		/** */
		PRIMITIVE;
	}

	public abstract static class OnParsedObject {

		public abstract void set(Object value) throws JOOS_ParsingException;
	}


	public abstract static class OnParsedValue {

		public abstract void set(String value) throws JOOS_ParsingException, ParsingException;
	}

	
	protected State state;
	
	
	/**
	 * 
	 * @author pc
	 *
	 */
	public static abstract class State {
		
		/**
		 * 
		 * @param parser for vertical movement (pushing a new scope, popping the current one and going back to parent one)
		 * @param reader for horizontal movement (crawling in the char stream)
		 * @return true: continue, false: stop
		 * @throws JOOS_ParsingException
		 * @throws IOException
		 */
		public abstract boolean parse(Parser parser, StreamReader reader, boolean isVerbose) 
				throws JOOS_ParsingException, IOException;

	}

	public abstract ScopeType getType();

	public abstract boolean isClosedBy(char c);
	
		
	

}
