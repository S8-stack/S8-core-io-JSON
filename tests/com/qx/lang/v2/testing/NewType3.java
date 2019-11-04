package com.qx.lang.v2.testing;

import com.qx.level0.lang.joos.JOOS_Field;
import com.qx.level0.lang.joos.JOOS_Type;

@JOOS_Type(name="NewType3")
public class NewType3 extends NewType2 {

	@JOOS_Field(name="str")
	public String str = "kdjooijso";
	
	@JOOS_Field(name="settings")
	public MyOtherObject2 settings = new MyOtherObject2();

}
