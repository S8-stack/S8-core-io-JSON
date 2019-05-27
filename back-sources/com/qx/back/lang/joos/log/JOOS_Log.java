package com.qx.back.lang.joos.log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * 
 * @author pc
 *
 */
public class JOOS_Log {
	
	
	public static final int MAX_NUMBER_OF_ENTRIES = 64;
	
	public class Entry {
		
		public int line;
		
		public String message;
		
		public Entry(int line, String message) {
			super();
			this.line = line;
			this.message = message;
		}
		
	}
	
	public class Warning extends Entry {

		public Warning(int line, String message) {
			super(line, message);
		}
		
		@Override
		public String toString(){
			return "WARNING(line="+line+"): "+message;
		}
	}
	
	public class BuildError extends Entry {

		public BuildError(int line, String message) {
			super(line, message);
		}
		
		@Override
		public String toString(){
			return "BD_ERROR(line="+line+"): "+message;
		}
	}
	
	public class DeserialisationError extends Entry {

		public DeserialisationError(int line, String message) {
			super(line, message);
		}
		
		@Override
		public String toString(){
			return "DZ_ERROR(line="+line+"): "+message;
		}
	}
	
	
	private List<Warning> warnings = new ArrayList<Warning>();
	
	private List<BuildError> bdErrors = new ArrayList<BuildError>();
	
	private List<DeserialisationError> dzErrors = new ArrayList<DeserialisationError>();

	
	public JOOS_Log() {
		super();
	}
	
	
	public Collection<Warning> getWarnings() {
		return warnings;
	}
	
	public Collection<BuildError> getBdErrors() {
		return bdErrors;
	}
	
	public Collection<DeserialisationError> getDzErrors() {
		return dzErrors;
	}
	
	
	public void addWarning(int line, String message){
		if(warnings.size()<MAX_NUMBER_OF_ENTRIES){
			warnings.add(new Warning(line, message));	
		}
	}
	
	public void addBuildError(int line, String message){
		if(bdErrors.size()<MAX_NUMBER_OF_ENTRIES){
			bdErrors.add(new BuildError(line, message));	
		}
	}
	
	public void addDeserializationError(int line, String message){
		if(dzErrors.size()<MAX_NUMBER_OF_ENTRIES){
			dzErrors.add(new DeserialisationError(line, message));	
		}
	}
	
	
}