package com.s8.blocks.joos.demos.repo00;

import com.s8.io.joos.JOOS_Field;
import com.s8.io.joos.JOOS_Type;

@JOOS_Type(name="myOtherObject2")
public class MyOtherObject2 {
	
	@JOOS_Field(name="coeficients")
	public double[] coefficients = new double[] {879, 87, 78, 98, -0.923};

	@JOOS_Field(name="str1")
	public String str1 = "kdjooijso";

	@JOOS_Field(name="str2")
	public String str2 = "kdjooijso";
	
	@JOOS_Field(name="coeficients2")
	public double[] coefficients2 = new double[] {879, 87, 78, 12};

	@JOOS_Field(name="str3")
	public String str3 = "kdjooijso";

	@JOOS_Field(name="str4")
	public String str4 = "kdjooijso";


}
