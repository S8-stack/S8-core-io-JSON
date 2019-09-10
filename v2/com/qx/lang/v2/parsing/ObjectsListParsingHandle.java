package com.qx.lang.v2.parsing;

import java.util.ArrayList;
import java.util.List;

import com.qx.lang.v2.Ws3dParsingException;

public class ObjectsListParsingHandle extends ParsingHandle {



	private ObjectsArraySetter setter;

	private List<Object> buffer;


	public ObjectsListParsingHandle(ObjectsArraySetter setter) {
		super();
		this.setter = setter;
		this.buffer = new ArrayList<>();
	}


	public void add(String value){
		buffer.add(value);
	}


	@Override
	public Setter getSetter(String name) throws Ws3dParsingException {
		return new PrimitiveSetter() {
			@Override
			public void set(String value) throws Ws3dParsingException {
				int index = Integer.valueOf(value);
				if(index!=buffer.size()){
					throw new Ws3dParsingException("Error in indexation");
				}
				buffer.add(value);
			}
		};
	}

	@Override
	public void close() throws Ws3dParsingException{
		setter.set(buffer);
	}


	@Override
	public boolean isList() {
		return true;
	}

}
