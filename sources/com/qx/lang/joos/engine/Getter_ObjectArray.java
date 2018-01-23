package com.qx.lang.joos.engine;

import java.io.OutputStreamWriter;

import com.qx.lang.joos.JOOS_NodeObject;
import com.qx.lang.joos.engine.Deserializer.LineCount;


public class Getter_ObjectArray extends Getter {
	

	/**
	 * 
	 */
	public Class<?> fieldType;
	
	

	
	public Getter_ObjectArray(Class<?> fieldType) {
		super();
		this.fieldType = fieldType;
	}


	@Override
	public void write(Serializer serializer, JOOS_NodeObject object, OutputStreamWriter writer, String indent, LineCount lineCount) throws Exception{
		
		String itemIndent = indent+"\t";
		writer.append(indent+annotatedName+" : ");
		
		try{
			Object[] array = (Object[]) method.invoke(object, NULL_PARAMATERS);
			if(array!=null){
				writer.append("[\n");
				int n = array.length;	
				lineCount.increment();
				for(int i=0; i<n; i++){
					writer.append(itemIndent);
					serializer.serialize((JOOS_NodeObject) array[i], writer, itemIndent);
					writer.append(i<n-1?",\n":"\n");
					lineCount.increment();
				}
				writer.append(indent+"]");
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
