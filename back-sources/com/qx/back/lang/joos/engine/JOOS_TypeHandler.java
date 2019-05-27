package com.qx.back.lang.joos.engine;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.qx.back.lang.joos.annotation.JOOS_Doc;
import com.qx.back.lang.joos.annotation.JOOS_Input;
import com.qx.back.lang.joos.annotation.JOOS_Object;
import com.qx.back.lang.joos.annotation.JOOS_Output;

/**
 * 
 * @author pc
 *
 */
public class JOOS_TypeHandler {

	/** JOOS name of the type */
	private String annotatedTypeName;

	/** constructor for new instances */
	private Constructor<?> constructor;


	/**
	 * 
	 */
	public Map<String, JOOS_TypeHandler> childTypes = new HashMap<String, JOOS_TypeHandler>();

	/**
	 * 
	 */
	public Map<String, Setter> setters = new HashMap<String, Setter>();

	/**
	 * 
	 */
	public Getter[] getters;


	/**
	 * 
	 */
	private Class<?> type;

	
	/** Type doc exposed to client */
	private String doc;


	/**
	 * 
	 */
	//private Getters[] getters;




	/**
	 * 
	 * @param type
	 * @param JAVAIndexedTypes
	 * @param localTypes
	 * 
	 * @return the type handler and create it if necessary
	 * @throws Exception 
	 */
	public static JOOS_TypeHandler get(Class<?> type,
			Map<String, JOOS_TypeHandler> JAVAIndexedTypes) throws Exception{

		String typeName = type.getName();

		// if type has no been already created
		if(!JAVAIndexedTypes.containsKey(typeName)){
			JOOS_TypeHandler handler = new JOOS_TypeHandler(type);
			JAVAIndexedTypes.put(typeName, handler);
			
			// initialize
			handler.initialize(JAVAIndexedTypes);
			return handler;
		}
		else{
			return JAVAIndexedTypes.get(typeName);
		}
	}





	/**
	 * 
	 * @param type
	 * @param types
	 * @throws Exception
	 */
	public JOOS_TypeHandler(Class<?> type){
		super();
		this.type = type;
	}
	
	
	
	private void initialize(Map<String, JOOS_TypeHandler> JAVAIndexedTypes) throws Exception{
		
		// get object annotation
		JOOS_Object typeAnnotation = type.getAnnotation(JOOS_Object.class);

		if(typeAnnotation==null){
			throw new Exception("Missing annotation in type: "+type.getName());
		}

		this.annotatedTypeName = typeAnnotation.name();
		

		// get object annotation
		JOOS_Doc docAnnotation = type.getAnnotation(JOOS_Doc.class);
		if(docAnnotation!=null){
			doc = docAnnotation.text();
		}
		// constructor
		if(!type.isInterface()){
			constructor = type.getConstructor(new Class<?>[]{});
		}

		// get line code metatdata field			
		List<Field> fieldsList = new ArrayList<Field>();
		for (Class<?> c = type; c != null; c = c.getSuperclass()) {
			if(!c.isInterface()){
				try{
					fieldsList.addAll(Arrays.asList(c.getDeclaredFields()));	
				}
				catch(Exception exception){
					throw new Exception(exception+" in "+type.getName());
				}
				catch(Error error){
					throw new Exception(error+" in "+type.getName());
				}
			}
		}
		int index=0;

		/*
		 * Getters & Setters
		 */

		Class<?> subType;

		Setter setter;
		JOOS_Input inputAnnotation;

		Getter getter;
		JOOS_Output outputAnnotation;


		List<Getter> bufferedGetters = new ArrayList<Getter>();

		// for each method
		for(Method method : type.getMethods()){

			// look for input (setter)
			inputAnnotation = method.getAnnotation(JOOS_Input.class);
			if(inputAnnotation!=null){
				setter = Setter.create(inputAnnotation, method, type);
				setters.put(inputAnnotation.name(), setter);
				if((subType=setter.getSubType())!=null){
					discoverChildType(subType, JAVAIndexedTypes, childTypes);
				}
			}


			// look for output (getter)
			outputAnnotation = method.getAnnotation(JOOS_Output.class);
			if(outputAnnotation!=null){
				getter = Getter.create(outputAnnotation, method);
				bufferedGetters.add(getter);
				if((subType=getter.getSubType())!=null){
					discoverChildType(subType, JAVAIndexedTypes, childTypes);
				}
			}

		}


		// reshuffle getters
		int n = bufferedGetters.size();
		getters = new Getter[n];
		for(Getter getterEntry : bufferedGetters){
			index = getterEntry.index;
			if (index>n-1){
				throw new Exception("Index out of range for getter entry: "+getterEntry.annotatedName+" for type "+
						type.getName());
			}
			else if (index<0){
				throw new Exception("Negative index not allowed for getter entry: "+getterEntry.annotatedName+" for type "+
						type.getName());
			}
			else if(getters[index]!=null){
				throw new Exception("Two Getters of type "+type.getName()+" have the same index:"
						+getterEntry.annotatedName+" and "+getters[index].annotatedName+".");
			}
			else{
				getters[index] = getterEntry;
			}
		}
		for(int i=0; i<n; i++){
			if(getters[i]==null){
				throw new Exception("No Getter with index "+i+".");
			}
		}

	}

	
	public String getDoc(){
		return doc;
	}
	
	/**
	 * Discover the type passed as argument and add it to childTypes
	 * Then, discover subTypes of this field type
	 * 
	 * @param type
	 * @param JAVAIndexedTypes
	 * @throws Exception
	 */
	public static void discoverChildType(
			Class<?> type,
			Map<String, JOOS_TypeHandler> JAVAIndexedTypes,
			Map<String, JOOS_TypeHandler> childTypes) throws Exception{

		JOOS_TypeHandler handler = get(type, JAVAIndexedTypes);
		String JOOS_Name = handler.annotatedTypeName;
		if(!childTypes.containsKey(JOOS_Name)){
			childTypes.put(JOOS_Name, handler);	
		}
		
		handler.discoverSubTypes(JAVAIndexedTypes, childTypes);
	}


	private void discoverSubTypes(
			Map<String, JOOS_TypeHandler> JAVAIndexedTypes,
			Map<String, JOOS_TypeHandler> parentChildTypes) throws Exception{

		// get object annotation
		JOOS_Object typeAnnotation = type.getAnnotation(JOOS_Object.class);
		JOOS_TypeHandler subTypeHandler;
		// subtypes
		Class<?>[] subTypes = typeAnnotation.sub();
		if(subTypes!=null){
			for(Class<?> subType : subTypes){
				subTypeHandler = get(subType, JAVAIndexedTypes);
				parentChildTypes.put(subTypeHandler.annotatedTypeName, subTypeHandler);
				
				// explore subTypes recursively
				subTypeHandler.discoverSubTypes(JAVAIndexedTypes, parentChildTypes);
			}
		}
	}
	
	public JOOS_TypeHandler getChildType(String JOOS_name) throws Exception{
		JOOS_TypeHandler handler = childTypes.get(JOOS_name);
		if(handler==null){
			throw new Exception("Type "+JOOS_name+" is unknown in the context of type "+annotatedTypeName);
		}
		return handler;
	}
	
	public Map<String, JOOS_TypeHandler> getChildTypes(){
		return childTypes;
	}
	

	public Setter getSetter(String fieldName) throws Exception{

		Setter setter = setters.get(fieldName);
		if(setter==null){
			throw new Exception("cannot set object value: cannot find setter <"+fieldName+"> in <"+type.getName()+">");
		}
		return setter;
	}


	public Object newInstance() throws Exception{
		return constructor.newInstance();
	}


	public String getAnnotatedTypeName(){
		return annotatedTypeName;
	}

	public String getTypeName(){
		return type.getName();
	}


}
