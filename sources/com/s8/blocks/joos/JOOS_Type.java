package com.s8.blocks.joos;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(value={ElementType.TYPE})
public @interface JOOS_Type {

	public String name();
	
	
	public Class<?>[] sub() default {};
	
}
