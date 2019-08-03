package com.qx.lang.joos;

import com.qx.lang.joos.log.JOOS_Log;



/**
 * Simple construction node on a JOOS objetc description
 * @author pc
 *
 */
public abstract class JOOS_NodeObject {

	
	private int inputCodeLine;

	public int getInputCodeLine() {
		return inputCodeLine;
	}

	public void setInputCodeLine(int inputCodeLine) {
		this.inputCodeLine = inputCodeLine;
	}
	
	
	private int outputCodeLine;

	public int getOutputCodeLine() {
		return outputCodeLine;
	}

	public void setOutputCodeLine(int outputCodeLine) {
		this.outputCodeLine = outputCodeLine;
	}
	
	private JOOS_Log log;
	
	public void setLog(JOOS_Log log){
		this.log = log;
	}
	
	public void issueWarning(String message){
		log.addWarning(getInputCodeLine(), message);
	}
	
	public void issueError(String message){
		log.addBuildError(getInputCodeLine(), message);
	}
	
}
