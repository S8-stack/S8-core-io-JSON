package com.s8.blocks.joos.tests;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;

import com.s8.alpha.models.S8Repo;
import com.s8.blocks.joos.JOOS_Lexicon;
import com.s8.blocks.joos.JOOS_PrimitiveExtension;
import com.s8.blocks.joos.parsing.JOOS_ParsingException;
import com.s8.blocks.joos.tests.repo00.MyRootType;
import com.s8.blocks.joos.types.JOOS_CompilingException;
import com.s8.blocks.joos.utilities.JOOS_BufferedFileReader;


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

		JOOS_Lexicon context = new JOOS_Lexicon();
		
		context.definePrimitiveExtension(new JOOS_PrimitiveExtension<S8Repo<?>>(S8Repo.class) {
			public @Override String serialize(S8Repo<?> value) {
				return value.toHexString();
			}

			public @Override S8Repo<?> deserialize(String str) {
				return null;
			}
		});
		
		context.discover(MyRootType.class);


		String pathname = "data/V2_test_input.joos";

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
