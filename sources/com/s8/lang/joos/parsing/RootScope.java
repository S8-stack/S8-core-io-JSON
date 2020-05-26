package com.s8.lang.joos.parsing;

import java.io.IOException;

public class RootScope extends ParsingScope {

	public Object result;

	public RootScope() {
		super();
		state = new State() {
			
			@Override
			public boolean parse(Parser parser, StreamReader reader, boolean isVerbose)
					throws JOOS_ParsingException, IOException {

				// initiate
				reader.readNext();

				// skip leading white spaces
				reader.skipWhiteSpaces();
				
				String name = reader.until(new char[] { ':' }, null, new char[] { '}', ')', '{', '('});
				
				if(name.equals("root")){
					
					// consume ':'
					reader.readNext();
					reader.skipWhiteSpaces();
					
					parser.pushScope(new ObjectScope(new OnParsedObject() {
						
						@Override
						public void set(Object value) throws JOOS_ParsingException {
							result = value;
						}
					}));
					
					state = new State() {
						
						@Override
						public boolean parse(Parser parser, StreamReader reader, boolean isVerbose)
								throws JOOS_ParsingException, IOException {
							parser.popScope();
							return false;
						}
					};
				}
				else{
					throw new JOOS_ParsingException("First declaration must be root, read: >"+name+"<");
				}
				return false;
			}
		};
	}


	@Override
	public ScopeType getType() {
		return ScopeType.MAPPED;
	}

	@Override
	public boolean isClosedBy(char c) {
		return '}'==c;
	}

}
