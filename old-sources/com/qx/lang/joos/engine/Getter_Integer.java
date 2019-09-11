package com.qx.lang.joos.engine;

import java.io.OutputStreamWriter;

import com.qx.lang.joos.JOOS_NodeObject;
import com.qx.lang.joos.engine.Deserializer.LineCount;




public class Getter_Integer extends Getter {

	@Override
	public void write(Serializer serializer, JOOS_NodeObject object, OutputStreamWriter writer, String indent, LineCount lineCount) throws Exception{
		writer.append(indent+annotatedName+" : ");	
		try{
			int i = (int) method.invoke(object, NULL_PARAMATERS);
			writer.append(String.valueOf(i));	
		}
		catch(Exception exception){
			writer.append(reformatErrorMessage(exception));
		}
		
	}

	@Override
	public Class<?> getSubType() {
		return null;
	}
}
