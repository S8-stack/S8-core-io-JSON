package com.qx.lang.v2;

import java.lang.reflect.Field;

import com.qx.lang.v2.annotation.WebScriptField;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;



public abstract class Ws3dFieldHandler {
	
	public enum Sort {
		PRIMITIVE, OBJECT, PRIMITIVES_LIST, OBJECTS_LIST;
	}
	

	/**
	 * 
	 */
	public String name;

	/**
	 * 
	 */
	public Field field;


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
	
	/*
	public abstract void set(
			Deserializer deserializer,
			Map<String, Ws3dTypeHandler> handlers,
			Object object,
			Ws3dStreamReader buffer) throws
			DeserializationException,
			IllegalAccessException,
			IllegalArgumentException;
			*/

	/**
	 * 
	 * @return the type to be explored next by the TypeHandler constructor.
	 */
	public abstract Class<?> getSubType();


	public abstract Sort getSort();

	/**
	 * 
	 * @param annotation : annotation of the field
	 * @param field : the field itself
	 * @param objectType : type of the parent object owning the field
	 * @return
	 * @throws Exception
	 */
	public static Ws3dFieldHandler create(Field field) {

		WebScriptField annotation = field.getAnnotation(WebScriptField.class);
		Class<?> fieldType = field.getType();

		Ws3dFieldHandler setter;

		// primitive
		if(fieldType.isPrimitive()){
			if(fieldType == double.class){
				setter = new DoubleWs3dFieldHandler();
			}
			else if(fieldType == int.class){
				setter = new IntegerWs3dFieldHandler();
			}
			else if(fieldType == boolean.class){
				setter = new BooleanWs3dFieldHandler();
			}
			else{
				throw new RuntimeException("Primitive type not supported "+fieldType.getName());
			}
		}
		// primitive
		else if(fieldType == String.class){
			setter = new StringWs3dFieldHandler();
		}
		// array
		else if(List.class.isAssignableFrom(fieldType)){
			Class<?> componentType = (Class<?>) fieldType.getGenericInterfaces()[0];

			// array of primitive
			if(componentType.isPrimitive()){
				/*
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
					throw new RuntimeException("Primitive array type not supported "+fieldType.getName());
				}
				*/
				throw new RuntimeException("Primitive array type not supported "+fieldType.getName());
			}
			// array of String
			else if(componentType == String.class){
				throw new RuntimeException("String array not supported "+fieldType.getName());
			}
			// array of object
			else{
				setter = new ObjectsListWs3dFieldHandler(componentType);
			}
		}
		// default to object
		else{
			setter = new ObjectWs3dFieldHandler(fieldType);	
		}

		setter.name = annotation.name();
		setter.field = field;
		return setter;
	}


	public String getName() {
		return name;
	}


}
