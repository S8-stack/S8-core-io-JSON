package com.s8.lang.joos.composing;

import java.io.IOException;

import com.s8.lang.joos.JOOS_Context;
import com.s8.lang.joos.type.TypeHandler;

public class ComposingScope {

	protected JOOS_Context context;

	protected JOOS_Writer writer;

	private char openingChar;

	private char closingChar;

	protected String indent;

	protected String enclosedIndent;

	protected String incrementalIndent;

	private boolean hasAlreadyLines = false;

	private boolean isInsertingLineFeed = false;

	public ComposingScope(
			JOOS_Context context,
			JOOS_Writer writer, 
			char openingChar, 
			char closingChar,
			boolean isInsertingLineFeed,
			String indent, 
			String incrementalIndent
			) {
		super();

		this.context = context;
		this.writer = writer;
		this.openingChar = openingChar;
		this.closingChar = closingChar;

		// compute indent
		this.indent = indent;
		this.incrementalIndent = incrementalIndent;
		this.enclosedIndent = indent+incrementalIndent;
		this.isInsertingLineFeed = isInsertingLineFeed;

	}



	public void open() throws IOException {
		writer.write(openingChar);
	}

	public void newItem() throws IOException {

		/*
		 * handle previous line separator
		 */
		if(hasAlreadyLines) {
			writer.write(',');
		}
		else {
			hasAlreadyLines = true;
		}

		if(isInsertingLineFeed) {
			// append new line
			writer.write('\n');

			// indent
			writer.write(enclosedIndent);	
		}
		else {
			// append white space
			writer.write(' ');
		}


	}


	public void append(String str) throws IOException {
		// value
		writer.write(str);
	}

	public void append(char c) throws IOException {
		writer.write(c);
	}

	public void close() throws IOException {
		if(hasAlreadyLines) {

			if(isInsertingLineFeed) {
				// append new line
				writer.write('\n');

				// indent
				writer.write(indent);		
			}
		}
		
		// value
		writer.write(closingChar);
	}


	public ComposingScope enterSubscope(char openingChar, char closingChar, boolean isInsertingLineFeed) {
		return new ComposingScope(context, writer, openingChar, closingChar, 
				isInsertingLineFeed, enclosedIndent, incrementalIndent);
	}




	public TypeHandler getTypeHandler(Object object) throws JOOS_ComposingException {
		return context.get(object.getClass());
	}



}
