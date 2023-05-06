package com.s8.io.joos.fields;

import java.lang.reflect.Method;


/**
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 *
 */
public abstract class SimpleFieldHandler extends FieldHandler {
	
	
	public static abstract class Builder extends FieldHandler.Builder {
		
		@Override
		public abstract SimpleFieldHandler getHandler();
		
		/**
		 * 
		 * @param method
		 */
		public void setSetter(Method method) {
			getHandler().setter = method;
		}
		
		
		public void setGetter(Method method) {
			getHandler().getter = method;
		}
	}
	
	@Override
	public Kind advertise() {
		return Kind.SIMPLE;
	}

	/**
	 * 
	 */
	public Method getter;
	
	
	public Method setter;
	
	
	
	public SimpleFieldHandler(String name) {
		super(name);
	}
	

}
