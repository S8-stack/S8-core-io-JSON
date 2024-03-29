package com.s8.core.io.json.parsing;

import java.io.IOException;

public abstract class ParsingState {
	

	/**
	 * 
	 * @param parser for vertical movement (pushing a new scope, popping the current one and going back to parent one)
	 * @param reader for horizontal movement (crawling in the char stream)
	 * @return true: continue, false: stop
	 * @throws JSON_ParsingException
	 * @throws IOException
	 */
	public abstract boolean parse(Parser parser, StreamReader reader, boolean isVerbose) 
			throws JSON_ParsingException, IOException;

}
