package com.s8.io.joos.utilities;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;

import com.s8.io.joos.parsing.JOOS_Reader;

/**
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 *
 */
public class JOOS_BufferedFileReader implements JOOS_Reader {


	private FileChannel channel;

	private boolean isEndOfFileReached;


	private ByteBuffer byteBuffer;

	/**
	 * char buffer is always left with capacity free.
	 */
	private CharBuffer charBuffer;

	private CharsetDecoder decoder;


	public JOOS_BufferedFileReader(FileChannel channel, Charset charset, int capacity) {
		this.channel = channel;
		decoder = charset.newDecoder();
		
		
		charBuffer = CharBuffer.allocate(capacity);
		// initialized as exhausted
		charBuffer.position(capacity);

		
		byteBuffer = ByteBuffer.allocate(capacity);
		// initialized as exhausted
		byteBuffer.position(capacity);

		isEndOfFileReached = false;
	}


	@Override
	public int read() throws IOException {

		while(!charBuffer.hasRemaining() && !isEndOfFileReached) {

			/* open for writing (by the channel) while preserving remaining -non decoded- bytes */
			byteBuffer.compact();

			// read
			int nBytes = channel.read(byteBuffer);

			/* return to read mode */
			byteBuffer.flip();

			if(nBytes==-1) {
				isEndOfFileReached = true;
			}

			charBuffer.clear();
			// for info: enfOfInput = isEndOfFileReached;
			CoderResult result = decoder.decode(byteBuffer, charBuffer, isEndOfFileReached);
			if(result.isMalformed() && result.isError()) {
				throw new IOException(result.toString());
			}
			charBuffer.flip();
		}

		if(charBuffer.hasRemaining()) {
			return charBuffer.get();
		}
		// no more chars remaining and no refill possible
		else if(isEndOfFileReached){
			return -1;
		}
		else {
			throw new IOException("Weird case...");
		}
	}






	public void close() throws IOException {


		// char buffer has been emptied, so clear
		charBuffer.clear();

		byteBuffer.clear();

		/**
		 * Force writing to the disk
		 */
		channel.force(true);


		channel.close();

	}

}
