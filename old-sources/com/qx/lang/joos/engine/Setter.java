package com.qx.lang.joos.engine;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import com.qx.lang.joos.annotation.JOOS_Input;
import com.qx.lang.joos.engine.Deserializer.LineCount;
import com.qx.lang.joos.log.JOOS_Log;



public abstract class Setter {

	/**
	 * 
	 */
	public String annotatedName;

	/**
	 * 
	 */
	public Method method;


	/**
	 * 
	 * @param deserializer
	 * @param object
	 * @param buffer
	 * @param lineCount 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 * @throws Exception
	 */
	public abstract void set(
			Deserializer deserializer,
			Map<String, JOOS_TypeHandler> handlers,
			Object object,
			JOOS_CharBuffer buffer,
			LineCount lineCount,
			JOOS_Log log) throws
			DeserializationException,
			IllegalAccessException,
			IllegalArgumentException;

	/**
	 * 
	 * @return the type to be explored next by the TypeHandler constructor.
	 */
	public abstract Class<?> getSubType();



	public static Setter create(JOOS_Input annotation, Method method, Class<?> objectType) throws Exception{

		Class<?>[] parameters = method.getParameterTypes();
		if(parameters.length!=1){
			throw new Exception("Setter "+method.getName()+" in type "+objectType.getName()+" has a wrong number of parameters");
		}
		Class<?> fieldType = parameters[0]; 

		Setter setter;

		// primitive
		if(fieldType.isPrimitive()){
			if(fieldType == double.class){
				setter = new Setter_Double();
			}
			else if(fieldType == int.class){
				setter = new Setter_Integer();
			}
			else if(fieldType == boolean.class){
				setter = new Setter_Boolean();
			}
			else{
				throw new Exception("Primitive type not supported "+fieldType.getName());
			}
		}
		// primitive
		else if(fieldType == String.class){
			setter = new Setter_String();
		}
		// array
		else if(fieldType.isArray()){
			Class<?> componentType = fieldType.getComponentType();


			// array of primitive
			if(componentType.isPrimitive()){
				if(componentType == double.class){
					setter = new Setter_DoubleArray();
				}
				else if(componentType == int.class){
					setter = new Setter_IntegerArray();
				}
				else if(componentType == boolean.class){
					setter = new Setter_BooleanArray();
				}
				else{
					throw new Exception("Primitive array type not supported "+fieldType.getName());
				}	
			}
			// array of String
			else if(componentType == String.class){
				setter = new Setter_StringArray();
			}
			// array of object
			else{
				setter = new Setter_ObjectArray(componentType);
			}
		}
		// default to object
		else{
			setter = new Setter_Object(fieldType);	
		}

		setter.annotatedName = annotation.name();
		setter.method = method;
		return setter;
	}


}
