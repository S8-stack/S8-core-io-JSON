package com.qx.back.lang.joos.engine;


import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
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
public class Setter_ObjectArray extends Setter {


	private Class<?> componentType;


	public Setter_ObjectArray(Class<?> componentType) {
		super();
		this.componentType = componentType;
	}


	@Override
	public void set(
			Deserializer deserializer,
			Map<String, JOOS_TypeHandler> handlers,
			Object object,
			JOOS_CharBuffer buffer,
			LineCount lineCount,
			JOOS_Log log) throws DeserializationException, IllegalAccessException, IllegalArgumentException {

		boolean isFinished = false;
		char c;
		//StringBuilder builder = new StringBuilder();
		List<Object> values = null;
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
				values = new ArrayList<Object>();
				values.add(deserializer.deserialize(buffer, handlers, log).node);
				//buffer.moveToNext();
				break;

			case ']':
				isFinished = true;
				buffer.moveToNext();
				break;

			case ',':
				values.add(deserializer.deserialize(buffer, handlers, log).node);
				//buffer.moveToNext();
				break;

			case ':':
			case ')':
			case ';':
				throw new DeserializationException(lineCount.get(), "Illegal end of definition for a double");



			default:
				if(!Deserializer.isValueCharacter(c)){
					throw new DeserializationException(lineCount.get(), "illegal character: "+c+"in Object Array Setter");
				}
		
				if(Deserializer.IS_VERBOSE){ System.out.print(c); }
				buffer.moveToNext();
				break;
			}
		}

		int n = values.size();
		Object array = Array.newInstance(componentType, n);
		for(int i=0; i<n; i++){
			Array.set(array, i, values.get(i));
		}
		try {
			method.invoke(object, array);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new DeserializationException(lineCount.get(), "Cannot set Object array due to "+e.getMessage());
		}
	}

	@Override
	public Class<?> getSubType() {
		return componentType;
	}

}