package com.qx.back.lang.joos.engine;

import java.io.OutputStreamWriter;

import com.qx.back.lang.joos.JOOS_NodeObject;
import com.qx.back.lang.joos.engine.Deserializer.LineCount;



public class Getter_Object extends Getter {


	/**
	 * 
	 */
	public Class<?> fieldType;

	
	public Getter_Object(Class<?> fieldType) {
		super();
		this.fieldType = fieldType;
	}

	@Override
	public void write(Serializer serializer, JOOS_NodeObject object, OutputStreamWriter writer, String indent, LineCount lineCount) throws Exception{
		writer.append(indent+annotatedName+" : ");
		try{
			JOOS_NodeObject fieldObjet = (JOOS_NodeObject) method.invoke(object, NULL_PARAMATERS);
			if(fieldObjet!=null){
				serializer.serialize(fieldObjet, writer, indent);		
			}
			else{
				writer.append("null");
			}
		}
		catch(Exception exception){
			writer.append(reformatErrorMessage(exception));
		}
		
	}
	
	@Override
	public Class<?> getSubType() {
		return fieldType;
	}
}
