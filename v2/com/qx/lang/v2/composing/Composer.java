package com.qx.lang.v2.composing;

import java.io.IOException;
import java.io.Writer;

import com.qx.lang.v2.JOOS_Context;
import com.qx.lang.v2.type.TypeHandler;

public class Composer {


	private JOOS_Context context;
	
	private Writer writer;
	
	private String indentSequence;


	public Composer(JOOS_Context context, Writer writer, String indentSequence, boolean isVerbose) {
		super();

		this.context = context;
		
		this.writer = writer;
		
		this.indentSequence = indentSequence;
	}




	public void compose(Object object) throws IllegalArgumentException, IllegalAccessException, IOException {
		writer.append("root:");
		ComposingScope scope = new RootComposingScope(context, writer, "\t");
		
		
		TypeHandler typeHandler = context.get(object.getClass());
		typeHandler.compose(object, scope);
		
	}

}
