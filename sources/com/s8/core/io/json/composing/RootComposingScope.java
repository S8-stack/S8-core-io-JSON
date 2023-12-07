package com.s8.core.io.json.composing;

import com.s8.core.io.json.JSON_Lexicon;


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
			JSON_Lexicon context,
			JSON_Writer writer, 
			String incrementalIndent) {
		super(context, writer, '{', '}', true, "", incrementalIndent);

	}

	@Override
	public ComposingScope enterSubscope(char openingChar, char closingChar, boolean isInsertingLineFeed) {
		return new ComposingScope(context, writer, openingChar, closingChar, 
				isInsertingLineFeed, "", incrementalIndent);
	}

}
