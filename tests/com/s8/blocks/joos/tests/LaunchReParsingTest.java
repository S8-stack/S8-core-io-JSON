package com.s8.blocks.joos.tests;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;

import com.s8.blocks.joos.JOOS_Lexicon;
import com.s8.blocks.joos.parsing.JOOS_ParsingException;
import com.s8.blocks.joos.tests.repo00.MyRootType;
import com.s8.blocks.joos.types.JOOS_CompilingException;
import com.s8.blocks.joos.utilities.JOOS_BufferedFileReader;

public class LaunchReParsingTest {

	public static void main(String[] args) throws IOException, JOOS_CompilingException {
		
		JOOS_Lexicon context = new JOOS_Lexicon();
		context.discover(MyRootType.class);
		
		
		String pathname = "data/V2_test_output.joos";

		
		RandomAccessFile file = new RandomAccessFile(new File(pathname), "r");
		
		
		try {
			JOOS_BufferedFileReader reader = new JOOS_BufferedFileReader(file.getChannel(), StandardCharsets.UTF_8, 64);
			
			MyRootType result = (MyRootType) context.parse(reader, true);
			System.out.println(result);	
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