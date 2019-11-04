package com.qx.lang.v2.testing;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import com.qx.level0.lang.joos.JOOS_Context;

public class LaunchComposingTest {

	public static void main(String[] args) throws IOException {

		NewType nt = new NewType();
		nt.a = 186;

		NewType2 nt2 = new NewType2();
		nt2.a = 0;
		nt2.aliases = new String[] { "ouqjoijoij", "lkjlkjlj817çuk", "kpkspok++"};
		nt.nt2 = nt2;
		
		NewType3 nt3 = new NewType3();
		nt3.a = 0;
		nt3.aliases = new String[] { "ouqjoijoij", "lkjlkjlj817çuk", "kpkspok++"};
		nt3.str = "Hello!!!";
		
		int nb=8;
		NewType2[] array = new NewType2[4*nb];
		for(int i=0; i<nb; i++) {
			array[i] = i%2==0?nt2:nt3;
		}
		nt.array = array;


		JOOS_Context context = new JOOS_Context();
		context.discover(NewType.class);


		String pathname = "testing/V2_test_ouput.joos";
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(pathname))));


		try {
			context.compose(writer, nt, "\t", true);
		}
		catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}

		writer.close();
	}
}
