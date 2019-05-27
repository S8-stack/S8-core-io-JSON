package com.qx.lang.joos.tests.example;

import com.qx.back.lang.joos.JOOS_NodeObject;
import com.qx.back.lang.joos.annotation.JOOS_Input;
import com.qx.back.lang.joos.annotation.JOOS_Object;
import com.qx.back.lang.joos.annotation.JOOS_Output;

@JOOS_Object(name="Sub object B-type 2", sub={})
public class MySubObjectB_Type2 extends MySubObjectB {


	private double a2;

	@SuppressWarnings("unused")
	private MyRootObjectA param;

	@JOOS_Input(name="a2")
	public void setField4(double a2){
		this.a2 = a2;
	}


	@JOOS_Output(i=0, name="a2")
	public double getField4(){
		return a2;
	}

	@JOOS_Output(i=1, name="a2 damned squared")
	public double getField42(){
		return a2*a2;
	}

	@JOOS_Output(i=2, name="poly")
	public double[] getField4_Poly(){
		return new double[]{0, 1 , a2, a2*a2};
	}

	@JOOS_Output(i=3, name="line")
	public int getLine(){
		return getInputCodeLine();
	}

	@JOOS_Input(name="special")
	public void setField5(MyRootObjectA param){
		this.param = param;
	}

	public Toto toto;

	@JOOS_Input(name="toto")
	public void setToto(Toto toto){
		this.toto = toto;
		System.out.println("Toto is ok");
	}

	@JOOS_Object(name="Toto", sub={})
	public static class Toto extends JOOS_NodeObject{

		public double b;

		@JOOS_Input(name="b")
		public void setB(double a){
			this.b = a;
		}
	}
}
