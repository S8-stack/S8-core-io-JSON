package com.qx.lang.joos.engine;

import java.io.OutputStreamWriter;

import com.qx.lang.joos.JOOS_NodeObject;
import com.qx.lang.joos.engine.Deserializer.LineCount;



public class Getter_String extends Getter {
	
	@Override
	public void write(Serializer serializer, JOOS_NodeObject object, OutputStreamWriter writer, String indent, LineCount lineCount) throws Exception{
		String string = null;
		try{
			string = (String) method.invoke(object, NULL_PARAMATERS);
		}
		catch(Exception exception){
			string = reformatErrorMessage(exception);
		}
		writer.append(indent+annotatedName+" : "+string);
	}

	
	@Override
	public Class<?> getSubType() {
		return null;
	}
}
