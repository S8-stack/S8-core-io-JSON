package com.s8.blocks.joos.demos.repo00;

import com.s8.core.io.json.JSON_Field;
import com.s8.core.io.json.JSON_Type;

@JSON_Type(name="newType2", sub= {NewType3.class})
public class NewType2 {

	@JSON_Field(name="a")
	public int a=-98798776;
	
	@JSON_Field(name="d")
	public double d=12;
	
	@JSON_Field(name="aliases")
	public String[] aliases;

}
