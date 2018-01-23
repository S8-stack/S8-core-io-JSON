package com.qx.lang.joos.engine;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class JOOS_CharBuffer {
	
	/** current position in the buffer*/
	private int index;
	
	/** length of the buffer */
	private int length;
	
	/** the char buffer */
	private char[] array;
	
	public JOOS_CharBuffer(String text){
		index = 0;
		array = text.toCharArray();
		length = text.length();
	}
	
	public boolean isNotTheEnd(){
		return index<length;
	}
	
	public char read(){
		return array[index];
	}
	
	public void moveToNext(){
		index++;
	}
	
	
	
	public static JOOS_CharBuffer loadFile(String pathname) throws IOException{
		InputStreamReader reader = new InputStreamReader(new FileInputStream(new File(pathname)));
		char[] buffer = new char[1024];
		int length;
		StringBuilder builder = new StringBuilder();
		while((length=reader.read(buffer))!=-1){
			builder.append(buffer, 0, length);
		}
		reader.close();
		return new JOOS_CharBuffer(builder.toString());
	}
	
	
	public static void main(String[] args) throws Exception{
		InputStreamReader reader = new InputStreamReader(new FileInputStream(new File("data/test/test.joos")));
		char[] buffer = new char[1024];
		int length;
		StringBuilder builder = new StringBuilder();
		while((length=reader.read(buffer))!=-1){
			builder.append(buffer, 0, length);
		}
		reader.close();
		System.out.println(builder);
	}
}
