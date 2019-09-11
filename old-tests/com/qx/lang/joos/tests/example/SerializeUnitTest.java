package com.qx.lang.joos.tests.example;

import java.io.OutputStreamWriter;

import com.qx.lang.joos.JOOS_Buildable;
import com.qx.lang.joos.JOOS_NodeObject;
import com.qx.lang.joos.engine.JOOS_Engine;
import com.qx.lang.joos.engine.Deserializer.Result;


public class SerializeUnitTest {
	
	public static void main(String[] args) throws Exception{
		JOOS_Engine engine = new JOOS_Engine();
		engine.discover(MyRootObjectA.class);
		
		Result result = engine.deserializeFile("data/test/test.joos");
		
		if(result.node!=null){

			JOOS_NodeObject object = result.node;
			
			((JOOS_Buildable) object).build();
			OutputStreamWriter writer = new OutputStreamWriter(System.out);

			//writer.write("Hi");
			engine.serialize(object, writer);
			writer.flush();
				
		}
		//result.log.toJSON(null);
	}

}
