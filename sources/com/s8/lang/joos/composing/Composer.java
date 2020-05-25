package com.s8.lang.joos.composing;

import java.io.IOException;

import com.s8.lang.joos.JOOS_Context;
import com.s8.lang.joos.type.TypeHandler;

public class Composer {


	private JOOS_Context context;
	
	private JOOS_Writer writer;
	
	private String indentSequence;

	public Composer(JOOS_Context context, JOOS_Writer writer, String indentSequence, boolean isVerbose) {
		super();
		this.context = context;
		this.writer = writer;		
		this.indentSequence = indentSequence;
	}


	public void compose(Object object) throws IllegalArgumentException, IllegalAccessException, IOException {
		writer.write("root:");
		ComposingScope scope = new RootComposingScope(context, writer, indentSequence);
		
		
		TypeHandler typeHandler = context.get(object.getClass());
		typeHandler.compose(object, scope);
		
	}

}
