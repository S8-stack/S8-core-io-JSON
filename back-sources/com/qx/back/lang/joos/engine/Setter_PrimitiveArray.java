package com.qx.back.lang.joos.engine;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.qx.back.lang.joos.engine.Deserializer.LineCount;
import com.qx.back.lang.joos.log.JOOS_Log;


/**
 * 
 * @author pc
 *
 */
public abstract class Setter_PrimitiveArray extends Setter {

	@Override
	public void set(
			Deserializer deserializer,
			Map<String, JOOS_TypeHandler> handlers,
			Object object,
			JOOS_CharBuffer buffer,
			LineCount lineCount,
			JOOS_Log log) throws DeserializationException {

		boolean isFinished = false;
		char c;
		StringBuilder builder = new StringBuilder();
		List<String> values = null;

		while(!isFinished && buffer.isNotTheEnd()){
			switch(c=buffer.read()){

			// increment line count on new line
			case '\n':
				lineCount.increment();

				// skip tab and ill-formed end of line (\r)
			case '\t':
			case '\r':
				buffer.moveToNext(); 
				break;

			case '[':
				values = new ArrayList<String>();
				buffer.moveToNext();
				break;

			case ']':
				values.add(Deserializer.prune(builder.toString()));
				isFinished = true;
				buffer.moveToNext();
				break;

			case ',':
				values.add(Deserializer.prune(builder.toString()));
				builder = new StringBuilder();
				buffer.moveToNext();
				break;


			case ':':
			case ')':
			case ';':
				log.addBuildError(lineCount.get(), "Illegal end of definition for a primitive array");
				break;

			default:
				if(!Deserializer.isValueCharacter(c)){
					throw new DeserializationException(lineCount.get(), "illegal character: "+c);
				}
				builder.append(c);
				if(Deserializer.IS_VERBOSE){ System.out.print(c); }
				buffer.moveToNext();
				break;
			}
		}

		set(object, values, lineCount.get());
	}


	/**
	 * 
	 * @param object
	 * @param value
	 * @throws Exception
	 */
	public abstract void set(Object object, List<String> values, int line) throws DeserializationException;

	@Override
	public Class<?> getSubType() {
		return null;
	}
}
