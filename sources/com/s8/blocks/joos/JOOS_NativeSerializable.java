package com.s8.blocks.joos;


/**
 * Not currently used
 * @author pc
 *
 */
public interface JOOS_NativeSerializable {

	public final static String PROTOTYPE_KEYWORD = "JOOS_PROTOTYPE";
	
	public interface Prototype {
	
		public JOOS_NativeSerializable deserialize();
		
	}
	
	
	public String serialize();
}
