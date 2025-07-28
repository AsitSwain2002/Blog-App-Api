package com.org.Blog_App_Api.ExceptionHandler;

public class JwtTimeExpairedException extends RuntimeException {

	public JwtTimeExpairedException(String message) {
		super(message);
	}

}
