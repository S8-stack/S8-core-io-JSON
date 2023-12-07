package com.s8.blocks.joos.demos.repo00;

import java.util.List;
import java.util.Map;

import com.s8.core.io.json.JSON_Field;
import com.s8.core.io.json.JSON_Type;

@JSON_Type(name="newType")
public class MyRootType {

	@JSON_Field(name="a")
	public int a=-98798776;
	
	@JSON_Field(name="d")
	public double d=12;
	
	
	public @JSON_Field(name="sub") NewType2 nt2;
	
	@JSON_Field(name="array")
	public NewType2[] array;
	
	
	public @JSON_Field(name="others") List<NewType2> others;
	
	public @JSON_Field(name="map") Map<String, NewType2> map;
	
	public @JSON_Field(name="pointers") Map<String, Integer> pointers;
	
	
}
