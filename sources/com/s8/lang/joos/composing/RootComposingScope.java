package com.s8.lang.joos.composing;

import com.s8.lang.joos.JOOS_Context;

public class RootComposingScope extends ComposingScope {


	public RootComposingScope(
			JOOS_Context context,
			JOOS_Writer writer, 
			String incrementalIndent) {
		super(context, writer, '{', '}', "", incrementalIndent);

	}

	@Override
	public ComposingScope enterSubscope(char openingChar, char closingChar) {
		return new ComposingScope(context, writer, openingChar, closingChar, "", incrementalIndent);
	}

}
