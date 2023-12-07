package com.s8.blocks.joos.demos.repo00;

import com.s8.core.io.json.JSON_Field;
import com.s8.core.io.json.JSON_Type;


public @JSON_Type(name="myOtherObject2") class MyOtherObject2 {
	
	
	/**
	 * coefficients
	 */
	@JSON_Field(name="coeficients")
	public double[] coefficients = new double[] {879, 87, 78, 98, -0.923};

	
	/**
	 * str1
	 */
	@JSON_Field(name="str1")
	public String str1 = "kdjooijso";

	
	/**
	 * str2
	 */
	@JSON_Field(name="str2")
	public String str2 = "kdjooijso";
	
	

	@JSON_Field(name="coeficients2")
	public double[] coefficients2 = new double[] {879, 87, 78, 12};

	@JSON_Field(name="str3")
	public String str3 = "kdjooijso";

	@JSON_Field(name="str4")
	public String str4 = "kdjooijso";


}
