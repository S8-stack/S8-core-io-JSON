package com.qx.back.lang.joos.engine;

import java.util.Map;

import com.qx.back.lang.joos.engine.Deserializer.LineCount;
import com.qx.back.lang.joos.log.JOOS_Log;


public abstract class Setter_Primitive extends Setter {

	@Override
	public void set(Deserializer deserializer,
			Map<String, JOOS_TypeHandler> handlers,
			Object object,
			JOOS_CharBuffer buffer,
			LineCount lineCount,
			JOOS_Log log) throws DeserializationException {
		boolean isFinished = false;
		char c;
		StringBuilder valueBuffer = new StringBuilder();
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

			case ',':
				isFinished = true;
				break;

			case '}':
				isFinished = true;
				break;

			case ')':
			case ';':
			case ']':
				throw new DeserializationException(lineCount.get(), "Illegal end of definition for a double");

			default:
				
				if((int) c!=0){
					if(!Deserializer.isValueCharacter(c)){
						throw new DeserializationException(lineCount.get(), "illegal character at line "+lineCount.get()+": "+c+" with code:"+((int) c));
					}
					valueBuffer.append(c);	
				}
				if(Deserializer.IS_VERBOSE){ System.out.print(c); }
				buffer.moveToNext();
				break;
			}
		}

		String valueToSet = Deserializer.prune(valueBuffer.toString());
		
		set(object, valueToSet, lineCount.get());			
		

	}


	/**
	 * 
	 * @param object
	 * @param value
	 * @throws DeserializationException 
	 * @throws Exception
	 */
	public abstract void set(Object object, String value, int line) throws DeserializationException;


	@Override
	public Class<?> getSubType() {
		return null;
	}
}
