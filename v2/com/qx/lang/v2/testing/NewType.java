package com.qx.lang.v2.testing;

import com.qx.lang.v2.annotation.WebScriptField;
import com.qx.lang.v2.annotation.WebScriptObject;

@WebScriptObject(name="NewType")
public class NewType {

	@WebScriptField(name="a")
	public int a=-98798776;
	
	@WebScriptField(name="d")
	public double d=12;
	
	@WebScriptField(name="sub")
	public NewType2 nt2;
	
	@WebScriptField(name="array")
	public NewType2[] array;
	
}
