package com.s8.lang.joos.type;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.s8.lang.joos.JOOS_Field;



public class FieldHandlerFactory {
	
	public static abstract class Extension {
		
		public abstract boolean isMatching(Class<?> fieldType);
		
		public abstract FieldHandler build(String name, Field field); 
	}
	
	
	private List<Extension> extensions = new ArrayList<>();
	
	public FieldHandlerFactory(Extension... extensions) {
		super();
	}
	
	public void add(Extension extension) {
		this.extensions.add(extension);
	}
	
	
	

	/**
	 * 
	 * @param annotation : annotation of the field
	 * @param field : the field itself
	 * @param objectType : type of the parent object owning the field
	 * @return
	 * @throws Exception
	 */
	public FieldHandler create(Field field) {

		JOOS_Field annotation = field.getAnnotation(JOOS_Field.class);
		
		String name = annotation.name();
		Class<?> fieldType = field.getType();
		
		for(Extension extension : extensions) {
			if(extension.isMatching(fieldType)) {
				return extension.build(name, field);
			}
		}
	
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
		// enum
		else if(fieldType.isEnum()){
			return new EnumFieldHandler(name, field);
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
}
