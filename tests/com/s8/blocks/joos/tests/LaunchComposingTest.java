package com.s8.blocks.joos.tests;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import com.s8.alpha.models.shells.S8Repo;
import com.s8.blocks.joos.JOOS_Lexicon;
import com.s8.blocks.joos.JOOS_PrimitiveExtension;
import com.s8.blocks.joos.composing.JOOS_ComposingException;
import com.s8.blocks.joos.tests.repo00.MyRootType;
import com.s8.blocks.joos.tests.repo00.NewType2;
import com.s8.blocks.joos.tests.repo00.NewType3;
import com.s8.blocks.joos.types.JOOS_CompilingException;
import com.s8.blocks.joos.utilities.JOOS_BufferedFileWriter;


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

		JOOS_Lexicon context = new JOOS_Lexicon();
		context.discover(MyRootType.class);
		context.definePrimitiveExtension(new JOOS_PrimitiveExtension<S8Repo<?>>(S8Repo.class) {
			@Override 
			public String serialize(S8Repo<?> value) {
				return value.toHexString();
			}
			@Override 
			public S8Repo<?> deserialize(String str) {
				//return S8ObjectRef.fromHexString(str);
				return null;
			}
		});



		String pathname = "data/V2_test_output.joos";
		RandomAccessFile file = new RandomAccessFile(new File(pathname), "rws");

		JOOS_BufferedFileWriter writer = new JOOS_BufferedFileWriter(file.getChannel(), StandardCharsets.UTF_8, 64);

		context.compose(writer, root, "\t", true);

		writer.close();
		file.close();
	}
}