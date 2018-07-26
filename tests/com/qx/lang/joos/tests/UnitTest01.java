package com.qx.lang.joos.tests;


import java.io.OutputStreamWriter;

import com.qx.lang.joos.JOOS_NodeObject;
import com.qx.lang.joos.engine.Deserializer.Result;
import com.qx.lang.joos.engine.JOOS_Engine;
import com.qx.lang.joos.tests.example.MyRootObjectA;


/**
 * 
 * @author pc
 *
 */
public class UnitTest01 {
	
	/**
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception{

		
		JOOS_Engine engine = new JOOS_Engine();
		engine.discover(MyRootObjectA.class);
		
		long time = System.nanoTime();
		Result result = engine.deserializeFile("testing/test_input.joos");
		JOOS_NodeObject object = result.node;
		
		
		OutputStreamWriter writer = new OutputStreamWriter(System.out);
		engine.serialize(object, writer);
		time = System.nanoTime() - time;
		writer.flush();
		
		
		System.out.println("\ndone: "+time/1e6+ " ms");
	}
		
}
