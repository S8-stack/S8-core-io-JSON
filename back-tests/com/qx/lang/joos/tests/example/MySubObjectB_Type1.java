package com.qx.lang.joos.tests.example;

import com.qx.back.lang.joos.JOOS_NodeObject;
import com.qx.back.lang.joos.annotation.JOOS_Input;
import com.qx.back.lang.joos.annotation.JOOS_Object;
import com.qx.back.lang.joos.annotation.JOOS_Output;

@JOOS_Object(name="Sub object B-type 1", sub={})
public class MySubObjectB_Type1 extends MySubObjectB {
	
	private double a1;

	@JOOS_Input(name="a1")
	public void setField4(double a1){
		this.a1 = a1;
	}
	
	private double[] tab = new double[]{1, 2};
	
	@JOOS_Input(name="tab")
	public void setTab(double[] array){
		this.tab = array;
	}
	
	@SuppressWarnings("unused")
	private Toto toto;
	
	@JOOS_Input(name="toto")
	public void setToto(Toto toto){
		this.toto = toto;
	}

	@JOOS_Output(i=0, name="a1 squared")
	public double getField4(){
		return a1*a1;
	}
	
	@JOOS_Output(i=1, name="line")
	public int getLine(){
		return getInputCodeLine();
	}
	
	@JOOS_Output(i=2, name="tab")
	public double[] getTab(){
		return tab;
	}
	
	
	@JOOS_Object(name="Toto", sub={})
	public static class Toto extends JOOS_NodeObject{
		
		public double a;
		
		@JOOS_Input(name="a")
		public void setA(double a){
			this.a = a;
		}
	}

	
}
