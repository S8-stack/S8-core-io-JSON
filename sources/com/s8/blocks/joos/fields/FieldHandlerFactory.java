package com.s8.blocks.joos.fields;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.s8.blocks.joos.JOOS_PrimitiveExtension;
import com.s8.blocks.joos.annotations.JOOS_Field;
import com.s8.blocks.joos.fields.arrays.BooleanArrayFieldHandler;
import com.s8.blocks.joos.fields.arrays.DoubleArrayFieldHandler;
import com.s8.blocks.joos.fields.arrays.FloatArrayFieldHandler;
import com.s8.blocks.joos.fields.arrays.IntegerArrayFieldHandler;
import com.s8.blocks.joos.fields.arrays.LongArrayFieldHandler;
import com.s8.blocks.joos.fields.arrays.ShortArrayFieldHandler;
import com.s8.blocks.joos.fields.arrays.StringArrayFieldHandler;
import com.s8.blocks.joos.fields.objects.ObjectFieldHandler;
import com.s8.blocks.joos.fields.objects.ObjectsArrayFieldHandler;
import com.s8.blocks.joos.fields.primitives.BooleanFieldHandler;
import com.s8.blocks.joos.fields.primitives.DoubleFieldHandler;
import com.s8.blocks.joos.fields.primitives.EnumFieldHandler;
import com.s8.blocks.joos.fields.primitives.FloatFieldHandler;
import com.s8.blocks.joos.fields.primitives.IntegerFieldHandler;
import com.s8.blocks.joos.fields.primitives.LongFieldHandler;
import com.s8.blocks.joos.fields.primitives.ShortFieldHandler;
import com.s8.blocks.joos.fields.primitives.StringFieldHandler;
import com.s8.blocks.joos.fields.structures.ObjectsListFieldHandler;
import com.s8.blocks.joos.fields.structures.ObjectsMapFieldHandler;
import com.s8.blocks.joos.parsing.JOOS_ParsingException;
import com.s8.blocks.joos.types.JOOS_CompilingException;



public class FieldHandlerFactory {

	


	private final List<JOOS_PrimitiveExtension<?>> extensions;

	public FieldHandlerFactory() {
		super();
		extensions = new ArrayList<JOOS_PrimitiveExtension<?>>();
	}

	public <T> void add(JOOS_PrimitiveExtension<T> extension) {
		extensions.add(extension);
	}




	/**
	 * 
	 * @param annotation : annotation of the field
	 * @param field : the field itself
	 * @param objectType : type of the parent object owning the field
	 * @return
	 * @throws JOOS_ParsingException 
	 * @throws Exception
	 */
	public FieldHandler create(Field field) throws JOOS_CompilingException {

		JOOS_Field annotation = field.getAnnotation(JOOS_Field.class);

		String name = annotation.name();
		Class<?> fieldType = field.getType();

		for(JOOS_PrimitiveExtension<?> extension : extensions) {
			if(extension.isMatching(fieldType)) {
				return extension.createFieldHandler(name, field);
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
					throw new JOOS_CompilingException(field.getDeclaringClass(), 
							"Primitives array type not supported "+componentType);
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
		else if(List.class.isAssignableFrom(fieldType)) {
			return new ObjectsListFieldHandler(name, field);
		}
		else if(Map.class.isAssignableFrom(fieldType)) {
			return new ObjectsMapFieldHandler(name, field);
		}
		// default to object
		else{
			return new ObjectFieldHandler(name, field);	
		}
	}
}
