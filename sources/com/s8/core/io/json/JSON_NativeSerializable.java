package com.s8.core.io.json;



/**
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public interface JSON_NativeSerializable {

	public final static String PROTOTYPE_KEYWORD = "JOOS_PROTOTYPE";
	
	public interface Prototype {
	
		public JSON_NativeSerializable deserialize();
		
	}
	
	
	public String serialize();
}
