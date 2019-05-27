package com.qx.back.lang.joos.engine;

import java.io.OutputStreamWriter;

import com.qx.back.lang.joos.JOOS_NodeObject;
import com.qx.back.lang.joos.engine.Deserializer.LineCount;




public class Getter_IntegerArray extends Getter {
	
	@Override
	public void write(Serializer serializer, JOOS_NodeObject object, OutputStreamWriter writer, String indent, LineCount lineCount) throws Exception{
		int[] array = (int[]) method.invoke(object, NULL_PARAMATERS);
		int n = array.length;
		writer.append(indent+annotatedName+" : [\n");	
		lineCount.increment();
		String itemIndent = indent+"\t";
		for(int i=0; i<n; i++){
			writer.append(itemIndent+String.valueOf(array[i])+(i<n-1?",\n":"\n"));
			lineCount.increment();
		}
		writer.append(indent+"]");	
	}
	
	@Override
	public Class<?> getSubType() {
		return null;
	}
}
