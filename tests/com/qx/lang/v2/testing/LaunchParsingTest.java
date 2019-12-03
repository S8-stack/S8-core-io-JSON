package com.qx.lang.v2.testing;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;

import com.qx.level0.lang.joos.JOOS_Context;
import com.qx.level0.lang.joos.JOOS_ParsingException;
import com.qx.level0.lang.joos.utilities.JOOS_BufferedFileReader;

public class LaunchParsingTest {

	public static void main(String[] args) throws IOException {
		
		JOOS_Context context = new JOOS_Context();
		context.discover(NewType.class);
		
		
		String pathname = "testing/V2_test_input.joos";
		
		RandomAccessFile file = new RandomAccessFile(new File(pathname), "r");
		
		
		try {
			JOOS_BufferedFileReader reader = new JOOS_BufferedFileReader(file.getChannel(), StandardCharsets.UTF_8, 64);
			
			NewType nt = (NewType) context.parse(reader, true);
			System.out.println(nt);	
			reader.close();
		}
		catch (JOOS_ParsingException e) {
			e.printStackTrace();
		}
		finally {
			
			file.close();
		}
	}
}
