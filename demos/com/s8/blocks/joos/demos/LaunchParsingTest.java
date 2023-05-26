package com.s8.blocks.joos.demos;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;

import com.s8.blocks.joos.demos.repo00.MyRootType;
import com.s8.io.joos.JOOS_Lexicon;
import com.s8.io.joos.parsing.JOOS_ParsingException;
import com.s8.io.joos.types.JOOS_CompilingException;
import com.s8.io.joos.utilities.JOOS_BufferedFileReader;


/**
 * 
 * @author pierreconvert
 *
 */
public class LaunchParsingTest {

	
	/**
	 * 
	 * @param args
	 * @throws IOException
	 * @throws JOOS_CompilingException
	 */
	public static void main(String[] args) throws IOException, JOOS_CompilingException {

		JOOS_Lexicon context = JOOS_Lexicon.from(MyRootType.class);
		

		System.out.println("Go!");
		


		String pathname = "data/V2_test_input2.js";

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
