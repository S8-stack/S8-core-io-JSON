package com.qx.lang.v2.testing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import com.qx.level0.lang.joos.JOOS_Context;
import com.qx.level0.lang.joos.JOOS_ParsingException;

public class LaunchParsingTest {

	public static void main(String[] args) throws IOException {
		
		JOOS_Context context = new JOOS_Context();
		context.discover(NewType.class);
		
		
		String pathname = "testing/V2_test_input.joos";
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(pathname))));
		
		
		try {

			NewType nt = (NewType) context.parse(reader, true);
			System.out.println(nt);	
		}
		catch (JOOS_ParsingException e) {
			e.printStackTrace();
		}
	}
}
