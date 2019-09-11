package com.qx.lang.joos.type;

import java.io.IOException;
import java.lang.reflect.Field;

import com.qx.lang.joos.JOOS_Context;
import com.qx.lang.joos.JOOS_Field;
import com.qx.lang.joos.composing.ComposingScope;



public abstract class FieldHandler {
	
	public enum ScopeType {
		PRIMITIVE, OBJECT, PRIMITIVES_ARRAY, OBJECTS_ARRAY;
	}
	

	/**
	 * 
	 */
	public String name;

	/**
	 * 
	 */
	public Field field;

	
	public FieldHandler(String name, Field field) {
		super();
		this.name = name;
		this.field = field;
	}
	

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


	public abstract ScopeType getScopeType();

	/**
	 * 
	 * @param annotation : annotation of the field
	 * @param field : the field itself
	 * @param objectType : type of the parent object owning the field
	 * @return
	 * @throws Exception
	 */
	public static FieldHandler create(Field field) {

		JOOS_Field annotation = field.getAnnotation(JOOS_Field.class);
		
		String name = annotation.name();
		Class<?> fieldType = field.getType();

		// primitive
		if(fieldType.isPrimitive()){
			if(fieldType == boolean.class){
				return new BooleanFieldHandler(name, field);
			}
			else if(fieldType == short.class){
				return new ShortFieldHandler(name, field);
			}
			else if(fieldType == int.class){
				return new IntegerFieldHandler(name, field);
			}
			else if(fieldType == long.class){
				return new LongFieldHandler(name, field);
			}
			else if(fieldType == float.class){
				return new FloatFieldHandler(name, field);
			}
			else if(fieldType == double.class){
				return new DoubleFieldHandler(name, field);
			}
			else{
				throw new RuntimeException("Primitive type not supported "+fieldType.getName());
			}
		}
		// primitive
		else if(fieldType == String.class){
			return new StringFieldHandler(name, field);
		}
		// array
		else if(fieldType.isArray()){
			Class<?> componentType = fieldType.getComponentType();

			// array of primitive
			if(componentType.isPrimitive()){
				if(componentType==boolean.class) {
					return new BooleanArrayFieldHandler(name, field);
				}
				else if(componentType==short.class) {
					return new ShortArrayFieldHandler(name, field);
				}
				else if(componentType==int.class) {
					return new IntegerArrayFieldHandler(name, field);
				}
				else if(componentType==long.class) {
					return new LongArrayFieldHandler(name, field);
				}
				else if(componentType==float.class) {
					return new FloatArrayFieldHandler(name, field);
				}
				else if(componentType==double.class) {
					return new DoubleArrayFieldHandler(name, field);
				}
				else {
					throw new RuntimeException("Primitives array type not supported "+componentType);
				}
			}
			else if(componentType==String.class) {
				return new StringArrayFieldHandler(name, field);
			}
			// array of object
			else{
				return new ObjectsArrayFieldHandler(name, field);
			}
		}
		// default to object
		else{
			return new ObjectFieldHandler(name, field);	
		}
	}


	public String getName() {
		return name;
	}


	public abstract void subDiscover(JOOS_Context context);

	public abstract boolean compose(Object object, ComposingScope scope) 
			throws IOException, IllegalArgumentException, IllegalAccessException;

}
