package com.qx.lang.joos.engine;

import java.io.OutputStreamWriter;

import com.qx.lang.joos.JOOS_NodeObject;
import com.qx.lang.joos.engine.Deserializer.LineCount;




public class Getter_Boolean extends Getter {
	
	@Override
	public void write(Serializer serializer, JOOS_NodeObject object, OutputStreamWriter writer, String indent, LineCount lineCount) throws Exception{
		boolean b = (boolean) method.invoke(object, NULL_PARAMATERS);
		writer.append(indent+annotatedName+" : "+String.valueOf(b));				
	}

	@Override
	public Class<?> getSubType() {
		return null;
	}
}
