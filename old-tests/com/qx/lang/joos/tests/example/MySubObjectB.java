package com.qx.lang.joos.tests.example;

import com.qx.lang.joos.JOOS_NodeObject;
import com.qx.lang.joos.annotation.JOOS_Input;
import com.qx.lang.joos.annotation.JOOS_Object;


@JOOS_Object(name="Sub object B", sub={MySubObjectB_Type1.class, MySubObjectB_Type2.class})
public abstract class MySubObjectB extends JOOS_NodeObject {
	

	
	@SuppressWarnings("unused")
	private double let;

	@JOOS_Input(name="leading edge thickness")
	public void setField3(double let){
		this.let = let;
	}
	
	@Override
	public String toString() {
		return "MySubObjectB, line = " + "not computed";
	}
}
