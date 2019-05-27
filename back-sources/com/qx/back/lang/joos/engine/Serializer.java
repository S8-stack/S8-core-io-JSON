package com.qx.back.lang.joos.engine;


import java.io.OutputStreamWriter;
import java.util.Map;

import com.qx.back.lang.joos.JOOS_NodeObject;
import com.qx.back.lang.joos.engine.Deserializer.LineCount;

public class Serializer {



	/**
	 * types
	 */
	private Map<String, JOOS_TypeHandler> types;
	

	private LineCount lineCount = new LineCount();


	public Serializer(Map<String, JOOS_TypeHandler> types){
		this.types = types;
	}


	public void serialize(JOOS_NodeObject object, OutputStreamWriter writer) throws Exception {
		lineCount.reset();
		writer.append("root : ");
		serialize(object, writer, "");
	}
	
	
	/**
	 * Serialize Object
	 * @param object
	 * @param writer
	 * @param indent
	 * @throws Exception
	 */
	public void serialize(JOOS_NodeObject object, OutputStreamWriter writer, String indent) throws Exception{
		if(object!=null){
			String typeName = object.getClass().getName();
			JOOS_TypeHandler handler = types.get(typeName);
			if(handler!=null){
				writer.append("("+handler.getAnnotatedTypeName()+"){\n");
				object.setOutputCodeLine(lineCount.get());
				lineCount.increment();
				Getter getter;

				int n = handler.getters.length;
				for(int i=0; i<n; i++){
					getter = handler.getters[i];
					getter.write(this, object, writer, indent+"\t", lineCount);
					writer.append(i<(n-1)?",\n":"\n");
					lineCount.increment();
				}
				writer.append(indent+"}");
			}	
		}
		else{
			writer.append("null");
		}
		
	}



	

}
