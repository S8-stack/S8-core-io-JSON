package com.s8.io.joos;



/**
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public interface JOOS_NativeSerializable {

	public final static String PROTOTYPE_KEYWORD = "JOOS_PROTOTYPE";
	
	public interface Prototype {
	
		public JOOS_NativeSerializable deserialize();
		
	}
	
	
	public String serialize();
}
