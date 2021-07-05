package com.s8.blocks.joos.fields.primitives;

public class JOOS_Syntax {
	
	
	
	
	public static String produceEscaped(String str) {
		StringBuilder builder = new StringBuilder();
		int n = str.length();
		for(int i=0; i<n; i++) {
			char c = str.charAt(i);
			switch(c) {
				case '\"' : builder.append( "\\\""); 
				break;
				
				case '{' : builder.append( "\\{"); 
				break;
				
				case '}' : builder.append( "\\}"); 
				break;
				
				case '(' : builder.append( "\\("); 
				break;
				
				case ')' : builder.append( "\\)"); 
				break;
				
				case '[' : builder.append( "\\[");
				break;
				
				case ']' : builder.append( "\\]");
				break;
				
				case ',' : builder.append( "\\,");
				break;
				
				default : builder.append(c); break;
			}
		}
		return builder.toString();
	}

}
