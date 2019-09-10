package com.qx.lang.v2.testing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import com.qx.lang.v2.Ws3dContext;
import com.qx.lang.v2.Ws3dParsingException;
import com.qx.lang.v2.parsing.Ws3dParser;
import com.qx.lang.v2.parsing.StreamReader;

public class LaunchTest {

	public static void main(String[] args) throws IOException {
		
		Ws3dContext context = new Ws3dContext();
		context.discover(NewType.class);
		
		
		String pathname = "testing/V2_test_input.joos";
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(pathname))));
		
		Ws3dParser parser = new Ws3dParser(context, new StreamReader(reader));
		
		try {

			NewType nt = (NewType) parser.parse();
			System.out.println(nt);	
		}
		catch (Ws3dParsingException e) {
			e.printStackTrace();
		}
	}
}
