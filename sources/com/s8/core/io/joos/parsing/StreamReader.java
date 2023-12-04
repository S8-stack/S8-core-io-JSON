package com.s8.core.io.joos.parsing;

import java.io.IOException;

/**
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 * @author pierreconvert
 *
 */
public class StreamReader {

	public static final boolean IS_DEBUG_ENABLED = true;

	private static final int TAB_LENGTH = 3;
	
	
	/**
	 * Quoted length must not exceed this length
	 */
	public final static int MAX_QUOTED_LENGTH = 8192;

	private JOOS_Reader reader;

	public int line;

	public int column;

	private boolean isRunning;

	/**
	 * 
	 */
	private int c;

	/**
	 * 
	 * @param reader
	 * @param filename: for debugging purposes
	 */
	public StreamReader(JOOS_Reader reader) {
		super();
		this.reader = reader;

		line = 1;
		column = 1;
		isRunning = true;
	}



	/**
	 * check (do not read next)
	 * 
	 * @param c
	 * @throws Exception
	 */
	public void check(char c) throws JOOS_ParsingException {
		if(this.c != c){
			throw new JOOS_ParsingException((char) this.c, line, column,
					"Expected char was: "+c);
		}
	}


	/**
	 * check (do not read next)
	 * 
	 * @param expectedChars
	 * @throws Exception
	 */
	public void check(char... expectedChars) throws JOOS_ParsingException {
		if(!isOneOf(expectedChars)){
			throw new JOOS_ParsingException(line, column, "Unexpected sequence encountered while deserializing");
		}
	}





	/**
	 * 
	 * @param sequence
	 * @throws JOOS_ParsingException
	 * @throws IOException
	 */
	public void checkSequence(String sequence) throws JOOS_ParsingException, IOException {
		if(isRunning){
			int n = sequence.length();
			for(int i = 0; i < n; i++) {
				char expectedChar = sequence.charAt(i);
				if(c != expectedChar) {
					throw new JOOS_ParsingException(line, column, "Sequence not matching");
				}
				moveNext();
			}
		}
		else{
			throw new JOOS_ParsingException(line, column, "Attempting to read closed stream");
		}
	}


	public void moveNext() throws IOException, JOOS_ParsingException {
		if(isRunning){
			boolean isNext = false;
			while(!isNext) {

				/* update position before consuming current char */
				if(c == '\n') { 
					line++; 
					column = 0;
				}
				else if(c == '\t') { 
					column+= TAB_LENGTH; 
				}
				else { 
					column++; 
				}

				/* read next char */
				c = reader.read();

				if(IS_DEBUG_ENABLED){
					System.out.print((char) c);
				}
				if(c==-1){
					isRunning = false;
					isNext = true;
					// end normally
				}
				else if(c=='\r'){ // always skipped
					// skipped
				}
				/*
				 * It's a zero-width no-break space.
				 * It's more commonly used as a byte-order mark (BOM).
				 */
				else if(c==65279){
					// skipped
				}
				else{
					isNext = true;
				}
			}	
		}
		else{
			throw new JOOS_ParsingException(line, column, "Attempting to read closed stream");
		}
	}


	/**
	 * WARNING : /!\Read from current char
	 * 
	 * @param skipped
	 * @throws IOException
	 * @throws XML_ParsingException
	 */
	public void skip(char... skipped) throws IOException, JOOS_ParsingException {
		while(isOneOf(skipped)){
			moveNext();
		}
	}
	
	



	public void moveToNextSymbol() throws JOOS_ParsingException, IOException {
		moveNext();
		skip('\n', '\t', ' ');
	}


	/**
	 * 
	 * @return
	 * @throws JOOS_ParsingException
	 * @throws IOException
	 */
	public String readAlphanumericChain() throws JOOS_ParsingException, IOException {
		StringBuilder builder = new StringBuilder();
		while(isAlphanumeric()) {
			builder.append((char) c);
			moveNext();
		}
		return builder.toString();
	}
	
	
	/**
	 * Started on opening quote mark
	 * @return
	 * @throws JOOS_ParsingException
	 * @throws IOException
	 */
	public String readQuotedChain() throws JOOS_ParsingException, IOException {
		
		/* consume opening quote mark */
		moveNext();
	
		StringBuilder builder = new StringBuilder();
		int count = 0, maxLength = MAX_QUOTED_LENGTH;
		while(c != '"' && count++ < maxLength) {
			builder.append((char) c);
			moveNext();
		}

		/* consume closing quote mark */
		moveNext();
		
		if(count == maxLength) { throw new IOException("Chain exceed quoted length"); }
		return builder.toString();
	}




	/**
	 * 
	 * @param values
	 * @return
	 */
	public boolean is(char c){
		return this.c == c;
	}

	/**
	 * 
	 * @param values
	 * @return
	 */
	public boolean isOneOf(char... values){
		if(values!=null){
			for(char value : values){
				if(c==value){
					return true;
				}
			}
			return false;
		}
		else{
			return false;
		}
	}





	public char getCurrentChar() {
		return (char) c;
	}



	/**
	 * Closes the stream and releases any system resources associated with it.
	 * Once the stream has been closed, further read(), ready(), mark(), reset(), 
	 * or skip() invocations will throw an IOException.
	 * Closing a previously closed stream has no effect.
	 * @throws IOException
	 */
	/*
	public void close() throws IOException {
		reader.close();
	}
	 */


	public boolean isFinished(){
		return c==-1;
	}


	// create function to check if alphanumeric
	public boolean isAlphanumeric(){
		return (c >= '0' & c <= '9') || // number
				(c >= 'a' && c <= 'z') || // letter lower case
				(c >= 'A' && c <= 'Z') || // letter (upper case)

				/* <special-characters> */
				c == '-' || 
				c == '_' || 
				c == '.' || 
				c == '&' || 
				c == '*' || 
				c == '$' || 
				c == '@'; 
		
	}

	@Override
	public String toString() {
		return "currently reading char: "+((char) c)+" at line="+line+", column="+column;
	}
}
