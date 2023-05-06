package com.s8.io.joos.fields;

import java.lang.reflect.Method;


/**
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 *
 */
public abstract class MapFieldHandler extends FieldHandler {
	
	
	public static abstract class Builder extends FieldHandler.Builder {
		
		@Override
		public abstract MapFieldHandler getHandler();
		
		/**
		 * 
		 * @param method
		 */
		public void setPutter(Method method) {
			getHandler().putter = method;
		}
		
		
		public void setTraverser(Method method) {
			getHandler().traverser = method;
		}
	}
	


	@Override
	public Kind advertise() {
		return Kind.MAP;
	}
	
	/**
	 * 
	 */
	public Method putter;
	
	
	public Method traverser;
	
	
	
	public MapFieldHandler(String name) {
		super(name);
	}
	

}
