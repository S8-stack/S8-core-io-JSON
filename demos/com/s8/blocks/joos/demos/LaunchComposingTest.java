package com.s8.blocks.joos.demos;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import com.s8.blocks.joos.demos.repo00.MyRootType;
import com.s8.blocks.joos.demos.repo00.NewType2;
import com.s8.blocks.joos.demos.repo00.NewType3;
import com.s8.io.joos.JOOS_Lexicon;
import com.s8.io.joos.composing.JOOS_ComposingException;
import com.s8.io.joos.types.JOOS_CompilingException;
import com.s8.io.joos.utilities.JOOS_BufferedFileWriter;


public class LaunchComposingTest {

	public static void main(String[] args) throws IOException, JOOS_ComposingException, JOOS_CompilingException {

		MyRootType root = new MyRootType();
		root.a = 186;

		NewType2 nt2 = new NewType2();
		nt2.a = 0;
		nt2.aliases = new String[] { "ouqjoijoij", "lkjlkjlj817çuk", "kpkspok++"};
		root.nt2 = nt2;

		NewType3 nt3 = new NewType3();
		nt3.a = 0;
		nt3.aliases = new String[] { "ouqjoijoij", "lkjlkjlj817çuk", "kpkspok++"};
		nt3.str = "Hello!!!";

		int nb=8;
		NewType2[] array = new NewType2[4*nb];
		for(int i=0; i<nb; i++) {
			array[i] = i%2==0?nt2:nt3;
		}
		root.array = array;

		root.others = new ArrayList<NewType2>();
		for(NewType2 newType2 : array) {
			root.others.add(newType2);			
		}

		JOOS_Lexicon context = JOOS_Lexicon.from(MyRootType.class);
		


		String pathname = "data/V2_test_output.js";
		RandomAccessFile file = new RandomAccessFile(new File(pathname), "rws");

		JOOS_BufferedFileWriter writer = new JOOS_BufferedFileWriter(file.getChannel(), StandardCharsets.UTF_8, 64);

		context.compose(writer, root, "\t", true);

		writer.close();
		file.close();
	}
}
