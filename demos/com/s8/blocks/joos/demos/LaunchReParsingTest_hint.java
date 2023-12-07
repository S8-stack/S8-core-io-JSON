package com.s8.blocks.joos.demos;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;

import com.s8.blocks.joos.demos.repo00.MyRootType;
import com.s8.core.io.json.JSON_Lexicon;
import com.s8.core.io.json.parsing.JSON_ParsingException;
import com.s8.core.io.json.types.JSON_CompilingException;
import com.s8.core.io.json.utilities.JOOS_BufferedFileReader;

public class LaunchReParsingTest_hint {

	public static void main(String[] args) throws IOException, JSON_CompilingException {
		
		JSON_Lexicon context = JSON_Lexicon.from(MyRootType.class);
		
		
		String pathname = "test-dataset/V2_test_output2.joos";

		
		RandomAccessFile file = new RandomAccessFile(new File(pathname), "r");
		
		
		try {
			JOOS_BufferedFileReader reader = new JOOS_BufferedFileReader(file.getChannel(), StandardCharsets.UTF_8, 64);
			
			MyRootType result = (MyRootType) context.parse(reader, MyRootType.class, true);
			System.out.println(result);	
			reader.close();
		}
		catch (JSON_ParsingException e) {
			e.printStackTrace();
		}
		finally {
			
			file.close();
		}
	}
}
