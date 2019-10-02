package com.qx.lang.joos.type;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.qx.lang.joos.JOOS_Context;
import com.qx.lang.joos.JOOS_Field;
import com.qx.lang.joos.JOOS_Type;
import com.qx.lang.joos.composing.ComposingScope;

/**
 * 
 * @author pc
 *
 */
public class TypeHandler {

	/**
	 * name of the type
	 */
	private String name;

	/**
	 * constructor for new instances
	 */
	private Constructor<?> constructor;

	/**
	 * child types
	 */
	public List<TypeHandler> subTypes = new ArrayList<>();

	/**
	 * field handlers
	 */
	public Map<String, FieldHandler> fieldHandlers = new HashMap<String, FieldHandler>();



	/**
	 * 
	 */
	private Class<?> type;


	/** Type doc exposed to client */
	//private String doc;



	/**
	 * 
	 * @param type
	 * @param types
	 * @throws Exception
	 */
	public TypeHandler(Class<?> type){
		super();
		this.type = type;
	}


	public void initialize(JOOS_Context context) {

		// get object annotation
		JOOS_Type typeAnnotation = type.getAnnotation(JOOS_Type.class);

		if(typeAnnotation==null){
			throw new RuntimeException("Missing annotation in type: "+type.getName());
		}

		name = typeAnnotation.name();

		// get object annotation
		/*
		JOOS_Doc docAnnotation = type.getAnnotation(JOOS_Doc.class);
		if(docAnnotation!=null){
			doc = docAnnotation.text();
		}
		 */

		// constructor
		if(!type.isInterface()){
			try {
				constructor = type.getConstructor(new Class<?>[]{});
			}
			catch (NoSuchMethodException | SecurityException e) {
				throw new RuntimeException("[Ws3dTypeHandler] No constructor for type: "+type.getName());
			}
		}


		/* <sub-types > */		

		subTypes = new ArrayList<>();
		getSubTypes(context, subTypes);

		/* </sub-types > */


		/* <fields> */

		FieldHandler fieldHandler;
		JOOS_Field fieldAnnotation;

		// for each method
		for(Field field : type.getFields()){

			// look for input (setter)
			fieldAnnotation = field.getAnnotation(JOOS_Field.class);
			if(fieldAnnotation!=null){

				// check if already existing
				if(fieldHandlers.get(fieldAnnotation.name())!=null){
					throw new RuntimeException("A field is already defined with name: "+fieldAnnotation.name());
				}

				// create field handler
				fieldHandler = context.getFieldFactory().create(field);

				fieldHandlers.put(fieldAnnotation.name(), fieldHandler);

				// explore recursively
				fieldHandler.subDiscover(context);
			}
		}
	}


	public void getSubTypes(JOOS_Context context, List<TypeHandler> types){


		TypeHandler subTypeHandler;
		JOOS_Type typeAnnotation = type.getAnnotation(JOOS_Type.class);

		Class<?>[] subTypes = typeAnnotation.sub();
		if(subTypes!=null){
			for(Class<?> subType : subTypes){
				subTypeHandler = context.get(subType);
				types.add(subTypeHandler);

				// explore subTypes recursively				
				subTypeHandler.getSubTypes(context, types);
			}
		}
	}



	public String getName(){
		return name;
	}

	public Object createInstance()
			throws
			InstantiationException,
			IllegalAccessException,
			IllegalArgumentException,
			InvocationTargetException
	{
		return constructor.newInstance(new Object[]{});
	}


	public FieldHandler getFieldHandler(String name) {
		return fieldHandlers.get(name);
	}
	
	
	public void compose(Object object, ComposingScope scope) 
			throws IllegalArgumentException, IllegalAccessException, IOException {
		
		// declare type
		scope.append('(');
		scope.append(name);
		scope.append(')');

		// begin body
		ComposingScope enclosedScope = scope.enterSubscope('{', '}');
		
		// write field
		enclosedScope.open();
		for(FieldHandler fieldHandler : fieldHandlers.values()) {
			fieldHandler.compose(object, enclosedScope);
		}
		enclosedScope.close();
	}
	
}
