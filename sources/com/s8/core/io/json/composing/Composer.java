package com.s8.core.io.json.composing;

import java.io.IOException;

import com.s8.core.io.json.JSON_Lexicon;
import com.s8.core.io.json.types.TypeHandler;



/**
 * 
 *
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public class Composer {


	private JSON_Lexicon context;
	
	private JSON_Writer writer;
	
	private String indentSequence;

	public Composer(JSON_Lexicon context, JSON_Writer writer, String indentSequence, boolean isVerbose) {
		super();
		this.context = context;
		this.writer = writer;		
		this.indentSequence = indentSequence;
	}


	public void compose(Object object) throws IOException, JSON_ComposingException {
		writer.write("const ROOT = ");
		ComposingScope scope = new RootComposingScope(context, writer, indentSequence);
		
		
		TypeHandler typeHandler = context.get(object.getClass());
		typeHandler.compose(object, scope, false);
		writer.write(";");
		
	}

}
