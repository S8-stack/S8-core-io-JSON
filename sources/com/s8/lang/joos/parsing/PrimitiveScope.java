package com.s8.lang.joos.parsing;

import java.io.IOException;

import com.s8.lang.joos.ParsingException;

public class PrimitiveScope extends ParsingScope {


	private OnParsedValue callback;
	
	public PrimitiveScope(OnParsedValue enclosing) {
		super();
		this.callback = enclosing;
		state = new State() {
			
			@Override
			public boolean parse(Parser parser, StreamReader reader, boolean isVerbose)
					throws JOOS_ParsingException, IOException {

				String value = null;
				if(reader.is('"')) {
					reader.readNext();
					boolean isAggregating = true;
					while(isAggregating) {
						String section = reader.until(new char[]{'"', '\\'}, null, new char[]{});
						if(value==null) {
							value = section;
						}
						else {
							value = value.concat(section);
						}
						if(reader.isCurrent('"')) {
							isAggregating = false;
						}
						else {
							reader.readNext();
							// and aggregate from the escaped char (inclusive)
						}
					}
					
					reader.readNext();
				}
				else {
					value = reader.until(new char[]{',', '}', ']'}, null, null);	
				}

				try{
					define(value);
				}
				catch (Exception e) {
					throw new JOOS_ParsingException(reader.line, reader.column, 
							"Cannot set Object due to "+e.getMessage()+", on string="+value);
				}
				
				/* pop this scope */
				parser.popScope();
				
				/* stop reading */
				return false;
			}
		};
	}
	
	
	public void define(String value) throws JOOS_ParsingException, ParsingException {
		callback.set(value);
	}
	

	@Override
	public ScopeType getType() {
		return ScopeType.PRIMITIVE;
	}


	@Override
	public boolean isClosedBy(char c) {
		return true;
	}

}
