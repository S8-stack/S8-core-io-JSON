package com.s8.io.joos.fields;

import java.lang.reflect.Method;


/**
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 *
 */
public abstract class ListFieldHandler extends FieldHandler {
	
	
	public static abstract class Builder extends FieldHandler.Builder {
		
		@Override
		public abstract ListFieldHandler getHandler();
		
		/**
		 * 
		 * @param method
		 */
		public void setAdder(Method method) {
			getHandler().adder = method;
		}
		
		
		public void setIterator(Method method) {
			getHandler().iterator = method;
		}
	}
	

	@Override
	public Kind advertise() {
		return Kind.LIST;
	}

	/**
	 * 
	 */
	public Method adder;
	
	
	public Method iterator;
	
	
	
	public ListFieldHandler(String name) {
		super(name);
	}
	

}
