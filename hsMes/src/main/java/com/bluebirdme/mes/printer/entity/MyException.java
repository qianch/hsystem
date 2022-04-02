package com.bluebirdme.mes.printer.entity;

public class MyException extends Exception{
	public MyException(String message){
		super(message);
		this.exceptionMessage=message;
	}
	private String exceptionMessage;

	public String getExceptionMessage() {
		return exceptionMessage;
	}

	public void setExceptionMessage(String exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
	}
	
}