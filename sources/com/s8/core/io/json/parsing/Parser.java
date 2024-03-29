package com.s8.core.io.json.parsing;

import java.io.IOException;
import java.util.Stack;

import com.s8.core.io.json.JSON_Lexicon;
import com.s8.core.io.json.types.TypeHandler;


/**
 * 
 * @author pc
 *
 *	handler -> setter
 *
 *
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 *
 */
public class Parser {

	private boolean isVerbose;

	private JSON_Lexicon context;

	private StreamReader reader;


	private Stack<ParsingScope> scopes;


	public Parser(JSON_Lexicon context, StreamReader reader, boolean isVerbose) {
		super();
		this.reader = reader;
		
		//rootBuilder = new RootParsedElement(context);

		this.isVerbose = isVerbose;
		this.context = context;
	}


	
	public JSON_Lexicon getContext() {
		return context;
	}
	
	
	public void pushScope(ParsingScope scope) {
		scopes.push(scope);
	}
	
	public void popScope() {
		scopes.pop();
	}

	/**
	 * 
	 * @return
	 * @throws XML_ParsingException 
	 * @throws IOException 
	 * @throws Exception
	 */
	public Object parse(Class<?> rootClassHint) throws JSON_ParsingException, IOException {
		
		// initialize reader
		reader.moveNext();
		
		scopes = new Stack<>();
		
		TypeHandler typeHandler = null;
		if(rootClassHint != null) {
			typeHandler = context.get(rootClassHint);
		}
		
		RootScope rootHandle = new RootScope(typeHandler);
		scopes.push(rootHandle);
		
		ParsingScope scope;
		while(!scopes.isEmpty()) {
			scope = scopes.peek();
			if(scope.state == null) {
				throw new JSON_ParsingException("No state defined for scope: "+scope);
			}
			while(scope.state.parse(this, reader, isVerbose));
		}
		return rootHandle.result;
	}
}
