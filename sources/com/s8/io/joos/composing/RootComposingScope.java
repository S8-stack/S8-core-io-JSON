package com.s8.io.joos.composing;

import com.s8.io.joos.JOOS_Lexicon;


/**
 * 
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 *
 */
public class RootComposingScope extends ComposingScope {


	public RootComposingScope(
			JOOS_Lexicon context,
			JOOS_Writer writer, 
			String incrementalIndent) {
		super(context, writer, '{', '}', true, "", incrementalIndent);

	}

	@Override
	public ComposingScope enterSubscope(char openingChar, char closingChar, boolean isInsertingLineFeed) {
		return new ComposingScope(context, writer, openingChar, closingChar, 
				isInsertingLineFeed, "", incrementalIndent);
	}

}
