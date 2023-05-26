package com.s8.blocks.joos.demos.repo00;

import com.s8.io.joos.JOOS_Field;
import com.s8.io.joos.JOOS_Type;

@JOOS_Type(name="newType3")
public class NewType3 extends NewType2 {

	@JOOS_Field(name="str")
	public String str = "kdjooijso";
	
	@JOOS_Field(name="settings")
	public MyOtherObject2 settings = new MyOtherObject2();

}
