package com.qx.lang.joos.tests.example;



import com.qx.back.lang.joos.JOOS_Buildable;
import com.qx.back.lang.joos.JOOS_NodeObject;
import com.qx.back.lang.joos.annotation.JOOS_Input;
import com.qx.back.lang.joos.annotation.JOOS_Object;
import com.qx.back.lang.joos.annotation.JOOS_Output;


@JOOS_Object(name="Root object A", sub={})
public class MyRootObjectA extends JOOS_NodeObject implements JOOS_Buildable {

	private MySubObjectA field1;

	private MySubObjectA field2;

	private MySubObjectB[] array;


	private int inputCodeLine;

	private int outputCodeLine;


	@SuppressWarnings("unused")
	private double a;


	@JOOS_Input(name="field 1")
	public void setField1(MySubObjectA field1){
		this.field1 = field1;
	}

	@JOOS_Input(name="field 2")
	public void setField2(MySubObjectA field2){
		this.field2 = field2;
	}

	@JOOS_Input(name="array")
	public void setArray(MySubObjectB[] array){
		this.array = array;
	}

	@JOOS_Output(i=0, name="array")
	public MySubObjectB[] getArray(){
		return array;
	}

	@JOOS_Input(name="parameter 1")
	public void setField3(double a){
		this.a = a;
	}

	public MyRootObjectA(){
	}


	@Override
	public String toString() {
		String s = "MyRootObjectA [field1=" + field1 + ", field2=" + field2 + ", inputLine=" + inputCodeLine + ", outputLine=" + outputCodeLine + "]";
		for(MySubObjectB object : array){
			s += "\n\t" + object.toString();
		}
		return s;
	}




	@Override
	public void build() {

		issueWarning("warning");
		issueWarning( "Long text warning with lots of cool information");

		issueError("java.lang.NumberFormatException: For input string: 'undefined'\n"+
				"at java.lang.NumberFormatException.forInputString(Unknown Source)\n"+
				"at java.lang.Integer.parseInt(Unknown Source)\n"+
				"at java.lang.Integer.valueOf(Unknown Source)\n"+
				"at com.mint.io.joosIDE.service.JOOS_WebService$12.process(JOOS_WebService.java:314)\n"+
				"at com.mint.io.joosIDE.service.JOOS_WebService.process(JOOS_WebService.java:398)\n"+
				"at com.mint.web.WebServer$Task.process(WebServer.java:348)\n"+
				"at com.mint.web.WebServer$Task.run(WebServer.java:231)\n");

	}

}
