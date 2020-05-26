package com.s8.lang.joos.parsing;

import java.io.IOException;
import java.util.Stack;

import com.s8.lang.joos.JOOS_Context;
import com.s8.lang.joos.JOOS_ParsingException;


/**
 * 
 * @author pc
 *
 *	handler -> setter
 *
 */
public class Parser {

	private boolean isVerbose;

	private JOOS_Context context;

	private StreamReader reader;


	private Stack<ParsingScope> scopes;


	public Parser(JOOS_Context context, StreamReader reader, boolean isVerbose) {
		super();
		this.reader = reader;
		//rootBuilder = new RootParsedElement(context);

		this.isVerbose = isVerbose;
		this.context = context;
	}


	
	public JOOS_Context getContext() {
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
	public Object parse() throws JOOS_ParsingException, IOException {
		scopes = new Stack<>();
		
		RootScope rootHandle = new RootScope();
		scopes.push(rootHandle);
		
		ParsingScope scope;
		while(!scopes.isEmpty()) {
			scope = scopes.peek();
			while(scope.state.parse(this, reader, isVerbose));
		}
		return rootHandle.result;
	}
}
