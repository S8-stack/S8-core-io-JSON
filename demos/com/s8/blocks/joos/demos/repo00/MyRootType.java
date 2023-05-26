package com.s8.blocks.joos.demos.repo00;

import java.util.List;
import java.util.Map;

import com.s8.io.joos.JOOS_Field;
import com.s8.io.joos.JOOS_Type;

@JOOS_Type(name="newType")
public class MyRootType {

	@JOOS_Field(name="a")
	public int a=-98798776;
	
	@JOOS_Field(name="d")
	public double d=12;
	
	
	public @JOOS_Field(name="sub") NewType2 nt2;
	
	@JOOS_Field(name="array")
	public NewType2[] array;
	
	
	public @JOOS_Field(name="others") List<NewType2> others;
	
	public @JOOS_Field(name="map") Map<String, NewType2> map;
	
	
}
