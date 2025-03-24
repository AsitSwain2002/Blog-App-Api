package com.org.Blog_App_Api.Util;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenericResponseHandler {

	private HttpStatus statusCode;
	private String message;
	private Object data;
	private int status;

	public ResponseEntity<?> create() {
		Map<String, Object> response = new LinkedHashMap<String, Object>();
		response.put("message", message);
		response.put("data", data);
		response.put("status", status);

		return new ResponseEntity<>(response, statusCode);
	}

}
