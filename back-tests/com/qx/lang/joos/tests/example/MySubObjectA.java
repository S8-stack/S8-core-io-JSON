package com.qx.lang.joos.tests.example;

import com.qx.back.lang.joos.JOOS_NodeObject;
import com.qx.back.lang.joos.annotation.JOOS_Input;
import com.qx.back.lang.joos.annotation.JOOS_Object;

@JOOS_Object(name="Sub object A", sub={})
public class MySubObjectA extends JOOS_NodeObject {
	
	
	@SuppressWarnings("unused")
	private double l;

	@JOOS_Input(name="length")
	public void setField3(double l){
		this.l = l;
	}

}
