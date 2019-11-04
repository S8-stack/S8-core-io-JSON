package com.qx.lang.v2.testing;

import com.qx.level0.lang.joos.JOOS_Field;
import com.qx.level0.lang.joos.JOOS_Type;

@JOOS_Type(name="NewType")
public class NewType {

	@JOOS_Field(name="a")
	public int a=-98798776;
	
	@JOOS_Field(name="d")
	public double d=12;
	
	@JOOS_Field(name="sub")
	public NewType2 nt2;
	
	@JOOS_Field(name="array")
	public NewType2[] array;
	
}
