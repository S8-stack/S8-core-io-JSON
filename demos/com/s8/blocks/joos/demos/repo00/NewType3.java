package com.s8.blocks.joos.demos.repo00;

import com.s8.core.io.json.JSON_Field;
import com.s8.core.io.json.JSON_Type;

@JSON_Type(name="newType3")
public class NewType3 extends NewType2 {

	@JSON_Field(name="str")
	public String str = "kdjooijso";
	
	@JSON_Field(name="settings")
	public MyOtherObject2 settings = new MyOtherObject2();

}
