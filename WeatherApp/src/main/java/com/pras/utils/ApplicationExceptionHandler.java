package com.pras.utils;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.pras.utils.Exceptions.WeatherException;

/**
 * Handler for custom exceptions
 * 
 * @author Prasanta
 *
 */
@RestControllerAdvice
public class ApplicationExceptionHandler {

	@ExceptionHandler(WeatherException.class)
	public ResponseEntity<Object> handleException(WeatherException ex) {
		 Map<String, Object> body = new HashMap<>();
		 body.put("error_code", ex.getErrorCode().getCode());
		 body.put("error_label", ex.getErrorCode().name());
	     body.put("message", ex.getMessage());
		return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
