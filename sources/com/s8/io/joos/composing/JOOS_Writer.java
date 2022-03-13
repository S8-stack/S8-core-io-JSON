package com.s8.io.joos.composing;

import java.io.IOException;


/**
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 *
 */
public interface JOOS_Writer {


	public void write(char c) throws IOException;
	
	public void write(String str) throws IOException;


}
