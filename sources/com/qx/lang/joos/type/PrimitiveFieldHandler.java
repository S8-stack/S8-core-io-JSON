package com.qx.lang.joos.type;

import java.io.IOException;
import java.lang.reflect.Field;

import com.qx.lang.joos.JOOS_Context;
import com.qx.lang.joos.ParsingException;
import com.qx.lang.joos.composing.ComposingScope;

/**
 * 
 * @author pc
 *
 */
public abstract class PrimitiveFieldHandler extends FieldHandler {

	
	
	
	public PrimitiveFieldHandler(String name, Field field) {
		super(name, field);
	}


	/**
	 * 
	 * @param object
	 * @param value
	 * @throws ParsingException 
	 * @throws Exception
	 */
	public abstract void parse(Object object, String value) throws ParsingException;
	
	
	
	
	
	/**
	 * 
	 * @param object
	 * @param writer
	 * @return has actually ouput something
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws IOException
	 */
	public abstract boolean compose(Object object, ComposingScope scope) 
			throws IllegalArgumentException, IllegalAccessException, IOException;

	public abstract String get(Object object) throws IllegalArgumentException, IllegalAccessException;

	@Override
	public Class<?> getSubType() {
		return null;
	}
	
	@Override
	public ScopeType getScopeType() {
		return ScopeType.PRIMITIVE;
	}
	
	
	@Override
	public void subDiscover(JOOS_Context context) {
		// nothing to sub-discover
	}
}
