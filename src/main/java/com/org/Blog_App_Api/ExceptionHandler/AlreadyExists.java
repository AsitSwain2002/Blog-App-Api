package com.org.Blog_App_Api.ExceptionHandler;

public class AlreadyExists extends RuntimeException {

	public AlreadyExists(String msg) {
		super(msg);
	}

}
