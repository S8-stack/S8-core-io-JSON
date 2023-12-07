package com.s8.core.io.json.parsing;

import java.io.IOException;

/**
 * 
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 *
 */
public abstract class PropsScope extends ParsingScope {

	@Override
	public ScopeType getType() {
		return ScopeType.MAPPED;
	}



	public abstract ParsingScope openProperty(String declarator) throws JSON_ParsingException;


	public PropsScope() {
		super();
		state = new ConsumeOpening();
	}



	public class ConsumeOpening extends ParsingState {


		public ConsumeOpening() {
			super();
		}

		@Override
		public boolean parse(Parser parser, StreamReader reader, boolean isVerbose)
				throws JSON_ParsingException, IOException {


			// skip any leading chars
			reader.skip('\n', '\t', ' ');

			/* previous state reading stopped on '{' (first item) or ',' (next items) */ 
			reader.check('{');

			/* consume */
			reader.moveNext();

			state = new ReadKey();

			return true;
		}
	}




	public class ReadKey extends ParsingState {


		public ReadKey() {
			super();
		}

		@Override
		public boolean parse(Parser parser, StreamReader reader, boolean isVerbose)
				throws JSON_ParsingException, IOException {


			reader.skip('\n', '\t', ' ');

			String declarator = null;
			if(reader.is('"')) {
				declarator = reader.readQuotedChain();
			}
			else if(reader.isAlphanumeric()){
				declarator = reader.readAlphanumericChain();
			}
			else if(reader.is('}')){

				/* consumed scope closing char */
				reader.moveNext();

				onExhausted();

				/* remove current scope from stack */
				parser.popScope();

				/* stop reading*/
				return false;
			}
			else {
				throw new JSON_ParsingException("Expect alphanumeric or quoted chain here");
			}


			reader.skip('\n', '\t', ' ');

			/* declarator mark mandatory at this point */
			reader.check(':');

			/* consume */
			reader.moveNext();

			/* dive into sub-scope */
			parser.pushScope(openProperty(declarator));

			/* setup state as read next entry for next time calling parse on this scope*/
			state = new ReadSeparator();	

			/* stop reading*/
			return false;

		}
	}



	public class ReadSeparator extends ParsingState {

		public ReadSeparator() {
			super();
		}

		@Override
		public boolean parse(Parser parser, StreamReader reader, boolean isVerbose)
				throws JSON_ParsingException, IOException {

			// 
			reader.skip('\n', '\t', ' ');

			if(reader.is(',')) { 
				reader.moveToNextSymbol();
			}

			state = new ReadKey();

			return true;
		}
	}





	public abstract void onExhausted() throws JSON_ParsingException;

}
