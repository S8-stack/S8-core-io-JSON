package com.s8.io.joos.composing;

import java.io.IOException;

import com.s8.io.joos.JOOS_Lexicon;
import com.s8.io.joos.types.TypeHandler;



/**
 * 
 *
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public class Composer {


	private JOOS_Lexicon context;
	
	private JOOS_Writer writer;
	
	private String indentSequence;

	public Composer(JOOS_Lexicon context, JOOS_Writer writer, String indentSequence, boolean isVerbose) {
		super();
		this.context = context;
		this.writer = writer;		
		this.indentSequence = indentSequence;
	}


	public void compose(Object object) throws IOException, JOOS_ComposingException {
		writer.write("const ROOT = ");
		ComposingScope scope = new RootComposingScope(context, writer, indentSequence);
		
		
		TypeHandler typeHandler = context.get(object.getClass());
		typeHandler.compose(object, scope, false);
		writer.write(";");
		
	}

}
