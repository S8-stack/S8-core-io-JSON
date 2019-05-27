package com.qx.back.lang.joos.engine;

import java.util.Map;

import com.qx.back.lang.joos.engine.Deserializer.LineCount;
import com.qx.back.lang.joos.log.JOOS_Log;




public class Setter_Object extends Setter {

	/**
	 * 
	 */
	public Class<?> fieldType;



	public Setter_Object(Class<?> fieldType) {
		super();
		this.fieldType = fieldType;
	}

	@Override
	public void set(
			Deserializer deserializer,
			Map<String, JOOS_TypeHandler> handlers,
			Object object,
			JOOS_CharBuffer buffer,
			LineCount lineCount,
			JOOS_Log log) throws DeserializationException {
		try {
			method.invoke(object, deserializer.deserialize(buffer, handlers, log).node);
		} catch (Exception e) {
			throw new DeserializationException(lineCount.get(), "Cannot set Object due to "+e.getMessage());
		}
	}

	@Override
	public Class<?> getSubType() {
		return fieldType;
	}

	
}
