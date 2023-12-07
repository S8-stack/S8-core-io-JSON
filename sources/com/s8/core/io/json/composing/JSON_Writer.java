package com.s8.core.io.json.composing;

import java.io.IOException;


/**
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 *
 */
public interface JSON_Writer {


	public void write(char c) throws IOException;
	
	public void write(String str) throws IOException;


}
