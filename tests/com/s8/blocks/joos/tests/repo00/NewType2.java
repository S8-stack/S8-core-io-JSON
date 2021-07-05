package com.s8.blocks.joos.tests.repo00;

import com.s8.blocks.joos.annotations.JOOS_Field;
import com.s8.blocks.joos.annotations.JOOS_Type;

@JOOS_Type(name="NewType2", sub= {NewType3.class})
public class NewType2 {

	@JOOS_Field(name="a")
	public int a=-98798776;
	
	@JOOS_Field(name="d")
	public double d=12;
	
	@JOOS_Field(name="aliases")
	public String[] aliases;

}
