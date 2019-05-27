package com.qx.back.lang.joos.engine;

import java.io.OutputStreamWriter;
import java.lang.reflect.Method;

import com.qx.back.lang.joos.JOOS_NodeObject;
import com.qx.back.lang.joos.annotation.JOOS_Output;
import com.qx.back.lang.joos.engine.Deserializer.LineCount;


public abstract class Getter {


	public final static Object[] NULL_PARAMATERS = new Object[]{};


	/**
	 * Sort the getters
	 */
	public int index;
	

	/**
	 * annotated field name
	 */
	public String annotatedName;

	/**
	 * 
	 */
	public Method method;




	/**
	 * 
	 * @param object
	 * @param writer
	 * @throws Exception
	 */
	public abstract void write(Serializer serializer, JOOS_NodeObject object, OutputStreamWriter writer, String indent, LineCount lineCount) throws Exception;

	/**
	 * 
	 * @return the type to be explored next by the TypeHandler constructor.
	 */
	public abstract Class<?> getSubType();

	public static Getter create(JOOS_Output annotation, Method method) throws Exception{

		Class<?> fieldType = method.getReturnType();

		Getter getter;

		// primitive
		if(fieldType.isPrimitive()){
			if(fieldType == double.class){
				getter = new Getter_Double();
			}
			else if(fieldType == int.class){
				getter = new Getter_Integer();
			}
			else if(fieldType == boolean.class){
				getter = new Getter_Boolean();
			}
			else{
				throw new Exception("Primitive type not supported "+fieldType.getName());
			}
		}
		// primitive
		else if(fieldType == String.class){
			getter = new Getter_String();
		}
		// array
		else if(fieldType.isArray()){
			Class<?> componentType = fieldType.getComponentType();


			// array of primitive
			if(componentType.isPrimitive()){
				if(componentType == double.class){
					getter = new Getter_DoubleArray();
				}
				else if(componentType == int.class){
					getter = new Getter_IntegerArray();
				}
				else if(componentType == boolean.class){
					getter = new Getter_BooleanArray();
				}
				else{
					throw new Exception("Primitive array type not supported "+fieldType.getName());
				}	
			}
			// array of String
			else if(componentType == String.class){
				getter = new Getter_StringArray();
			}
			// array of object
			else{
				getter = new Getter_ObjectArray(componentType);
			}
		}
		// default to object
		else{
			getter = new Getter_Object(fieldType);
		}

		getter.index = annotation.i();
		getter.annotatedName = annotation.name();
		getter.method = method;
		return getter;
	}
	
	
	public String reformatErrorMessage(Exception exception){
		exception.printStackTrace();
		String message = exception.getMessage();
		if(message!=null){
			int length = message.length();
			return "not computed due to: "+message.substring(0, Math.min(length, 256));
		}
		else{
			return "undefined";
		}
	}
}
