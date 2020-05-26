package com.s8.lang.joos.parsing;

import java.io.IOException;

import com.s8.lang.joos.JOOS_ParsingException;
import com.s8.lang.joos.JOOS_Syntax;


public class StreamReader {
	
	public static final boolean IS_DEBUG_ENABLED = true;

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
	 * @param sequence
	 * @throws IOException 
	 * @throws Exception
	 */
	public void check(String sequence) throws JOOS_ParsingException, IOException {
		int n=sequence.length();
		for(int i=0; i<n; i++){
			if(c!=sequence.charAt(i)){
				throw new JOOS_ParsingException(line, column,
						"Unexpected sequence encountered while deserializing");
			}
			if(i<n-1){
				readNext();	
			}
		}
	}

	/**
	 * check (do not read next)
	 * 
	 * @param c
	 * @throws Exception
	 */
	public void check(char c) throws JOOS_ParsingException {
		if(this.c!=c){
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
	 * WARNING : /!\Read from current char. Stop on one of <code>stopping</code>.
	 * @param stopping
	 * @param ignored
	 * @param forbidden
	 * @return
	 * @throws Exception
	 */
	public String until(char[] stopping, char[] ignored, char[] forbidden) throws JOOS_ParsingException, IOException{
		StringBuilder builder = new StringBuilder();
		while(true) {
			if(isOneOf(stopping)){
				return builder.toString();
			}
			else if(isOneOf(forbidden)){
				throw new JOOS_ParsingException(line, column,
						"Forbidden char has been found>"+((char)c)+"<");
			}
			else if(isOneOf(ignored)){
				// skipped
			}
			else{
				builder.append((char) c);
			}
			readNext();
		}
	}


	/**
	 * 
	 * @param ignored
	 * @param forbidden
	 * @throws Exception
	 */
	public void next(char[] ignored, char[] forbidden) throws JOOS_ParsingException, IOException {
		boolean isNext = false;
		while(!isNext) {
			readNext();

			if(isOneOf(forbidden)){
				throw new JOOS_ParsingException(line, column, "Forbidden char has been found");
			}
			else if(isOneOf(ignored)){
				// skipped
			}
			else{
				isNext = true;
			}
		}
	}

	public void next(char[] ignored) throws JOOS_ParsingException, IOException {
		boolean isNext = false;
		while(!isNext) {
			readNext();
			if(isOneOf(ignored)){
				// skipped
			}
			else{
				isNext = true;
			}
		}
	}


	/**
	 * base methof for reading next char
	 * @throws IOException 
	 * @throws Exception
	 */
	public void readNext() throws JOOS_ParsingException, IOException {
		if(isRunning){
			boolean isNext = false;
			while(!isNext) {
				c = reader.read();
				if(IS_DEBUG_ENABLED){
					System.out.print((char) c);
				}
				if(c==-1){
					isRunning = false;
					isNext = true;
					// end normally
				}
				else if(c=='\n'){
					line++;
					column=0;
					isNext = false; // skipped
				}
				else if(c=='\r'){
					// skipped
				}
				else if(c=='\t'){
					column+=5;
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
	 * read next char until reading end char
	 * 
	 * @param stoppingChars
	 * @return
	 * @throws IOException 
	 * @throws JOOS_ParsingException 
	 * @throws Exception
	 */
	public String readBlock(int length) throws JOOS_ParsingException, IOException {
		StringBuilder builder = new StringBuilder();
		int i=0;
		while(i<length){
			
			c = reader.read();
			
			if(IS_DEBUG_ENABLED){
				System.out.print((char) c);
			}
			
			builder.append((char) c);
			i++;
		}
		return builder.toString();
	}


	/**
	 * read next char until reading end char
	 * 
	 * @param stoppingChars
	 * @return
	 * @throws Exception
	 */
	public String readUntilOneOf(char... stoppingChars) throws Exception{
		StringBuilder builder = new StringBuilder();
		while(true){
			readNext();
			if(isOneOf(stoppingChars)){
				return builder.toString();	
			}
			else if(c==-1){
				throw new Exception("Unexpected end of stream");
			}
			else{
				builder.append((char) c);
			}
		}
	}
	
	
	/**
	 * 
	 * @param values
	 * @return
	 */
	public boolean is(char c){
		return this.c==c;
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


	public void readNextWhileIgnoring(char... ignoredChars) throws IOException, JOOS_ParsingException {
		readNext();
		if(IS_DEBUG_ENABLED){
			System.out.print((char) c);
		}
		while(isOneOf(ignoredChars)){
			c = reader.read();
		}
	}

	public void skipWhiteSpaces() throws IOException, JOOS_ParsingException {
		while(c==JOOS_Syntax.WHITE_SPACE){
			readNext();
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
			readNext();
		}
	}



	public char getCurrentChar() {
		return (char) c;
	}

	public boolean isCurrent(char comparedChar){
		return ((char) c)==comparedChar;
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
	
	
	@Override
	public String toString() {
		return "currently reading char: "+((char) c)+" at line="+line+", column="+column;
	}
}
