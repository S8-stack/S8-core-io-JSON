package com.qx.lang.v2;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.qx.lang.v2.annotation.WebScriptField;
import com.qx.lang.v2.annotation.WebScriptObject;

/**
 * 
 * @author pc
 *
 */
public class Ws3dTypeHandler {

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
	public List<Ws3dTypeHandler> subTypes = new ArrayList<>();

	/**
	 * field handlers
	 */
	public Map<String, Ws3dFieldHandler> fieldHandlers = new HashMap<String, Ws3dFieldHandler>();



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
	public Ws3dTypeHandler(Class<?> type){
		super();
		this.type = type;
	}


	public void initialize(Ws3dContext context) {

		// get object annotation
		WebScriptObject typeAnnotation = type.getAnnotation(WebScriptObject.class);

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

		Class<?> subType;

		Ws3dFieldHandler fieldHandler;
		WebScriptField fieldAnnotation;

		// for each method
		for(Field field : type.getFields()){

			// look for input (setter)
			fieldAnnotation = field.getAnnotation(WebScriptField.class);
			if(fieldAnnotation!=null){

				// check if already existing
				if(fieldHandlers.get(fieldAnnotation.name())!=null){
					throw new RuntimeException("A field is already defined with name: "+fieldAnnotation.name());
				}

				// create field handler
				fieldHandler = Ws3dFieldHandler.create(field);

				fieldHandlers.put(fieldAnnotation.name(), fieldHandler);

				// explore recursively
				if((subType=fieldHandler.getSubType())!=null){
					context.discover(subType);
				}
			}
		}
	}


	public void getSubTypes(Ws3dContext context, List<Ws3dTypeHandler> types){


		Ws3dTypeHandler subTypeHandler;
		WebScriptObject typeAnnotation = type.getAnnotation(WebScriptObject.class);

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


	public Ws3dFieldHandler getFieldHandler(String name) {
		return fieldHandlers.get(name);
	}

}
