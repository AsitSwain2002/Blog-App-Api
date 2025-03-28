package com.org.Blog_App_Api.Util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.org.Blog_App_Api.ExceptionHandler.ExceptionData;

public class ResponseBuilder {

	public static ResponseEntity<?> withMessage(String message, Object data, HttpStatus status) {
		GenericResponseHandler gs = new GenericResponseHandler();
		gs.setMessage(message);
		gs.setStatusCode(status);
		gs.setData(data);
		return gs.create();
	}

	public static ResponseEntity<?> withMessageNoData(String message, HttpStatus status) {
		GenericResponseHandler gs = new GenericResponseHandler();
		gs.setMessage(message);
		gs.setStatus(status.value());
		gs.setStatusCode(status);
		return gs.create();
	}

	public static ResponseEntity<?> withErrorMessage(String message, HttpStatus status) {
		GenericResponseHandler gs = new GenericResponseHandler();
		gs.setData(message);
		gs.setStatusCode(status);
		return gs.create();
	}

	public static ResponseEntity<?> withErrorMessage(ExceptionData message, HttpStatus status) {
		GenericResponseHandler gs = new GenericResponseHandler();
		gs.setData(message);
		gs.setStatusCode(status);
		gs.setMessage("Failed");
		gs.setStatus(status.value());
		return gs.create();
	}
}
