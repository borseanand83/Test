package com.ubs.opsit.interviews.exception;

public class ApplicationException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ApplicationException() {
		super();
	}
	
	public ApplicationException(String message){
		super(message);
	}

	public ApplicationException(String message,Exception e){
		super(message,e);
	}

	
	@Override
	public String getMessage() {
		return super.getMessage();
	}
	
	
}
