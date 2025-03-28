package com.org.Blog_App_Api.ExceptionHandler;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.org.Blog_App_Api.Util.ResponseBuilder;

@RestControllerAdvice
public class GenericExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<?> resourceNotFound(ResourceNotFoundException ex) {

		ExceptionData ed = new ExceptionData();
		ed.setStatus(HttpStatus.NOT_FOUND.value());
		ed.setMessage(ex.getMessage());
		ed.setTime(new Date().toLocaleString());
		return ResponseBuilder.withErrorMessage(ed, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(AlreadyExists.class)
	public ResponseEntity<?> alreadyExists(AlreadyExists al) {
		ExceptionData ed = new ExceptionData();
		ed.setMessage(al.getMessage());
		ed.setStatus(HttpStatus.CONFLICT.value());
		ed.setTime(new Date().toLocaleString());
		return ResponseBuilder.withErrorMessage(ed, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<?> IllegalArgumentException(IllegalArgumentException al) {
		ExceptionData ed = new ExceptionData();
		ed.setMessage(al.getMessage());
		ed.setStatus(HttpStatus.CONFLICT.value());
		ed.setTime(new Date().toLocaleString());
		return ResponseBuilder.withErrorMessage(ed, HttpStatus.CONFLICT);
	}
}
