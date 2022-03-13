package com.s8.io.joos.utilities;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;

import com.s8.io.joos.composing.JOOS_Writer;

/**
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 *
 */
public class JOOS_BufferedFileWriter implements JOOS_Writer {

	
	private FileChannel channel;

	private int capacity;

	private ByteBuffer byteBuffer;

	/**
	 * char buffer is always left with capacity free.
	 */
	private CharBuffer charBuffer;

	private CharsetEncoder encoder;


	public JOOS_BufferedFileWriter(FileChannel channel, Charset charset, int capacity) {
		this.channel = channel;
		encoder = charset.newEncoder();

		charBuffer = CharBuffer.allocate(2*capacity);
		byteBuffer = ByteBuffer.allocate(2*capacity);
		this.capacity = capacity;
	}

	
	@Override
	public void write(char c) throws IOException {
		
		charBuffer.put(c);
		
		enforceAvailability();
	}
	
	
	@Override
	public void write(String str) throws IOException {

		int offset=0, strLength=str.length();
		while(offset<strLength) {
			int n = Math.min(capacity, strLength-offset);
			charBuffer.put(str, offset, n);
			enforceAvailability();
			offset+=n;
		}
	}


	/**
	 * <p>
	 * <code>char buffer</code> must always be left with free capacity. This method
	 * pushes has many chars to the file as necessary to enforce the following rule:
	 * <code>charBuffer.remaining()>capacity</code>.
	 * </p>
	 * 
	 * @throws IOException
	 */
	private void enforceAvailability() throws IOException {

		/* position has gone beyond half buffer capacity, so more filled than empty
		 * -> Trigger flushing
		 * */
		if(charBuffer.remaining() < capacity) {

			// switch to read mode (prepare for encoding)
			charBuffer.flip();

			while(charBuffer.remaining() > capacity) {

				// no end of file
				CoderResult result = encoder.encode(charBuffer, byteBuffer, false);

				/*
				 *	Note that we only try a single push since the expensive operation is 
				 * the channel write 
				 */
				if(result.isOverflow()) {

					// switch to read mode (prepare for channel writing)
					byteBuffer.flip();

					// write
					channel.write(byteBuffer);

					// return to accumulate mode
					byteBuffer.compact();
				}
			}

			/*
			 * at this point, the remaining bytes that could be written to channel is < than capacity.
			 * Because charBuffer.capacity()=2*capacity, this means that free space is > than capacity, so ok.
			 */
			charBuffer.compact();
		}
	}


	public void close() throws IOException {

		// switch to read mode (prepare for encoding)
		charBuffer.flip();

		while(charBuffer.hasRemaining()) {

			// no end of file
			CoderResult result = encoder.encode(charBuffer, byteBuffer, true);

			/*
			 *	Note that we only try a single push since the expensive operation is 
			 * the channel write 
			 */
			if(result.isOverflow()) {

				// switch to read mode (prepare for channel writing)
				byteBuffer.flip();

				// write
				channel.write(byteBuffer);

				// return to accumulate mode
				byteBuffer.compact();
			}
		}

		// char buffer has been emptied, so clear
		charBuffer.clear();

		// switch to read mode (prepare for channel writing)
		byteBuffer.flip();

		while(byteBuffer.hasRemaining()) {

			// write
			channel.write(byteBuffer);
		}
		
		byteBuffer.clear();

		/**
		 * Force writing to the disk
		 */
		channel.force(true);
		
		
		channel.close();

	}

}
