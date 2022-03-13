package com.s8.io.joos;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value={ElementType.TYPE})
public @interface JOOS_Type {

	public String name();
	
	
	public Class<?>[] sub() default {};
	
}
