package com.qx.back.lang.joos.engine;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import com.qx.back.lang.joos.JOOS_NodeObject;
import com.qx.back.lang.joos.log.JOOS_Log;


public class Deserializer {



	public class Result{

		public JOOS_TypeHandler handler;

		public JOOS_NodeObject node;

		public JOOS_Log log;

		public Result(JOOS_TypeHandler handler, JOOS_NodeObject object, JOOS_Log log) {
			super();
			this.handler = handler;
			this.node = object;
			this.log = log;
		}

	}



	public static boolean IS_VERBOSE = false;

	public static String ROOT_KEYWORD = "root";

	private final static char[] ALLOWED_SPECIAL_CHAR = {'.', '-', ' ', '_'};




	/**
	 * types
	 */

	private LineCount lineCount = new LineCount();

	private Map<String, JOOS_TypeHandler> rootTypes;

	public Deserializer(Map<String, JOOS_TypeHandler> rootTypes){
		this.rootTypes = rootTypes;
	}



	public Result deserializeFile(String pathname) throws IOException, IllegalAccessException, IllegalArgumentException{
		return deserialize(getCode(pathname));
	}


	public Result deserialize(String code) {
		JOOS_CharBuffer buffer = new JOOS_CharBuffer(code);
		JOOS_Log log = new JOOS_Log();

		try{
			lineCount.reset();

			boolean isFinished = false;
			char c;
			StringBuilder builder = new StringBuilder();
			String keyword;

			while(!isFinished && buffer.isNotTheEnd()){
				switch(c=buffer.read()){

				// skip tab and new line
				case '\n':
					lineCount.increment();
				case '\t':
					buffer.moveToNext(); 
					break;

				case ':':

					try{
						keyword = prune(builder.toString());		
					}
					catch (Exception e) {
						throw new DeserializationException(lineCount.get(), "prune error: "+e.getMessage());
					}

					if(!keyword.equals(ROOT_KEYWORD)){
						throw new DeserializationException(lineCount.get(), "Definition is not strating with appropriate keyword (root)");
					}

					isFinished = true;
					buffer.moveToNext();
					return deserialize(buffer, rootTypes, log);

				case ';':
				case ']':
					throw new DeserializationException(lineCount.get(), "Illegal end of definition");

				default:
					if(!Deserializer.isValueCharacter(c)){
						throw new DeserializationException(lineCount.get(), "illegal character: "+c);
					}
					if(builder!=null){
						builder.append(c);	
					}
					if(Deserializer.IS_VERBOSE){ System.out.print(c); }
					buffer.moveToNext();
					break;
				}
			}

			throw new DeserializationException(0, "No object");
		}
		catch(DeserializationException exception){
			log.addDeserializationError(exception.getLine(), exception.getMessage());
		}
		catch (IllegalAccessException exception) {
			exception.printStackTrace();
			log.addDeserializationError(0, exception.getMessage());
		}
		catch (IllegalArgumentException exception) {
			exception.printStackTrace();
			log.addDeserializationError(0, exception.getMessage());
		}
		

		return new Result(null, null, log);
	}


	/**
	 * 
	 * @param handler: the handler of the object being built
	 * @param object: the object in the process of being built
	 * @param buffer: the buffer being read
	 * @throws DeserializationException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 * @throws Exception 
	 */
	public Result deserialize(JOOS_CharBuffer buffer, Map<String, JOOS_TypeHandler> handlers, JOOS_Log log) throws DeserializationException, IllegalAccessException, IllegalArgumentException{

		JOOS_TypeHandler handler = null;
		JOOS_NodeObject object = null;

		boolean isFinished = false;
		char c;
		StringBuilder builder = null;
		String JOOS_TypeName, keyword;

		Map<String, JOOS_TypeHandler> childHandlers = null;

		while(!isFinished && buffer.isNotTheEnd()){
			switch(c=buffer.read()){

			// increment line count on new line
			case '\n':
				lineCount.increment();

				// skip tab and ill-formed end of line (\r)
			case '\t':
			case '\r':
				buffer.moveToNext(); 
				break;

			case '(':
				builder = new StringBuilder();
				buffer.moveToNext();
				break;


			case ')':
				try{
					JOOS_TypeName = Deserializer.prune(builder.toString());	
				}
				catch (Exception exception) {
					throw new DeserializationException(lineCount.get(),
							"Cannot find JOOS Type name due to "+exception.getMessage());
				}
				handler = handlers.get(JOOS_TypeName);

				if(handler==null){
					throw new DeserializationException(lineCount.get(), "Type ("+JOOS_TypeName+") is not defined");
				}
				childHandlers = handler.getChildTypes();
				buffer.moveToNext();
				break;

			case '{':
				if(handler==null){
					log.addBuildError(lineCount.get(), "Type ("+lineCount.get()+") is not defined");
				}

				// build object
				try{
					object =  (JOOS_NodeObject) handler.newInstance();	
				}
				catch (Exception e){ 
					throw new DeserializationException(lineCount.get(), "Cannot instantiate "+handler.getAnnotatedTypeName()+" due to :"+e.getMessage());
				}
				object.setInputCodeLine(lineCount.get());
				object.setLog(log);

				builder = new StringBuilder();
				buffer.moveToNext();
				break;

			case ':':
				buffer.moveToNext();
				if(builder==null){
					throw new DeserializationException(lineCount.get(), "missing end of statement");
				}
				keyword = prune(builder.toString());
				Setter setter = null;
				try{
					setter = handler.getSetter(keyword);	
				}
				catch(Exception e){
					throw new DeserializationException(lineCount.get(), "Cannot find setter with key: "+keyword);
				}
				if(childHandlers==null){
					log.addBuildError(lineCount.get(), "Child handlers have not yet been discovered");
				}
				setter.set(this, childHandlers, object, buffer, lineCount, log);
				break;


			case ',':
				builder = new StringBuilder();
				buffer.moveToNext();
				break;

			case '}':
				buffer.moveToNext();
				isFinished = true;
				break;

			case ';':
			case ']':
				log.addBuildError(lineCount.get(), "Illegal end of definition for a double");
				buffer.moveToNext();
				break;

			default:

				if(builder!=null && ((int) c)!=0){
					if(!Deserializer.isValueCharacter(c)){
						log.addBuildError(lineCount.get(), "illegal character: >"+c+"< with code "+((int) c)+" in Deserializer");
					}
					builder.append(c);	
				}
				if(Deserializer.IS_VERBOSE){ System.out.print(c); }
				buffer.moveToNext();
				break;
			}
		}

		return new Result(handler, object, log);		
	}




	/*
	 * Utilities
	 */

	private final static String TEXT_ENCODING = "utf8";
	private final static int BUFFER_SIZE = 1024;



	/**
	 * Retrieve the code and remove all new line and tabs characters
	 * @param pathname
	 * @return
	 * @throws IOException
	 */
	private static String getCode(String pathname) throws IOException{
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(pathname), TEXT_ENCODING), BUFFER_SIZE);
		String line;
		StringBuilder builder = new StringBuilder();
		while ((line = bufferedReader.readLine()) != null) {
			builder.append(line+"\n");
		}
		bufferedReader.close();

		String code = builder.toString();
		return code;
	}


	public static String prune(String word){

		if(word.length()==0){
			return "";
		}

		int i0=0;
		while(i0<word.length() && word.charAt(i0)==' '){
			i0++;
		}

		if(i0==word.length()){
			return "";
		}

		int i1=word.length();
		while(i1>=0 && word.charAt(i1-1)==' '){
			i1--;
		}
		return word.substring(i0, i1);
	}


	public static boolean isOneOf(char testedCharacter, char[] characters){
		for(char character : characters){
			if(testedCharacter == character){
				return true;
			}
		}
		return false;
	}



	public static boolean isValueCharacter(char c){
		return Character.isLetterOrDigit(c) || isOneOf(c, ALLOWED_SPECIAL_CHAR);
	}


	public static void test01() throws IOException{

		InputStreamReader reader = new InputStreamReader(new FileInputStream(new File("data/test/test.joos")));
		char[] buffer = new char[1024];
		int length;
		while((length=reader.read(buffer))!=-1){
			for(int i=0; i<length; i++){
				System.out.print(buffer[i]);
			}
		}
		reader.close();


		String a = "jhhihuiuh";
		a.toCharArray();
	}

	public static class LineCount{
		int i=1;

		public int get(){
			return i;
		}

		public void reset() {
			i=1;
		}

		public void increment(){
			i++;
		}
	}
}
