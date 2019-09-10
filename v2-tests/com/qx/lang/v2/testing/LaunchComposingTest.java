package com.qx.lang.v2.testing;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import com.qx.lang.v2.JOOS_Context;

public class LaunchComposingTest {

	public static void main(String[] args) throws IOException {

		NewType nt = new NewType();
		nt.a = 186;

		NewType2 nt2 = new NewType2();
		nt2.a = 0;
		nt2.aliases = new String[] { "ouqjoijoij", "lkjlkjlj817çuk", "kpkspok++"};
		nt.nt2 = nt2;
		
		int nb=8;
		NewType2[] array = new NewType2[nb];
		for(int i=0; i<nb; i++) {
			array[i] = nt2;
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
