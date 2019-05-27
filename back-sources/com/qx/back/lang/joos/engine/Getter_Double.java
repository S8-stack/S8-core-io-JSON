package com.qx.back.lang.joos.engine;

import java.io.OutputStreamWriter;

import com.qx.back.lang.joos.JOOS_NodeObject;
import com.qx.back.lang.joos.engine.Deserializer.LineCount;




public class Getter_Double extends Getter {
	
	@Override
	public void write(Serializer serializer, JOOS_NodeObject object, OutputStreamWriter writer, String indent, LineCount lineCount) throws Exception{

		writer.append(indent+annotatedName+" : ");
		try{
			double d = (double) method.invoke(object, NULL_PARAMATERS);
			writer.append(Double.toString(d));
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
