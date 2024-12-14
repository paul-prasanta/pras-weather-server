package com.pras.utils;

/**
 * Exception and error codes for this application
 * 
 * @author Prasanta
 *
 */
public class Exceptions {
	
	/**
	 * Error Codes for client side visibility and tracking
	 */
	public static enum ErrorCode {
		ERROR(100), NO_RECORD_FOUND(101), API_ERROR(102), ACCESS_DENIED(103);
		
		int code;
		ErrorCode(int code) {
			this.code = code;
		}
		
		public int getCode() {
			return code;
		}
	}

	/**
	 * Base exception for generic issues
	 */
	public static class WeatherException extends RuntimeException {
		protected ErrorCode errorCode = ErrorCode.ERROR;
		
		public WeatherException() {
			super();
		}

		public WeatherException(String message) {
			super(message);
		}
		
		public WeatherException(String message, ErrorCode errorCode) {
			super(message);
			this.errorCode = errorCode;
		}

		public ErrorCode getErrorCode() {
			return errorCode;
		}
	}
	
	/**
	 * Exception if no records are found
	 */
	public static class RecordNotFoundException extends WeatherException {

		public RecordNotFoundException() {
			super();
			this.errorCode = ErrorCode.NO_RECORD_FOUND;
		}

		public RecordNotFoundException(String message) {
			super(message);
			this.errorCode = ErrorCode.NO_RECORD_FOUND;
		}
	}
	
	/**
	 * Exception if problem in Provider API access
	 */
	public static class ApiException extends WeatherException {
		public ApiException() {
			super();
			this.errorCode = ErrorCode.API_ERROR;
		}

		public ApiException(String message) {
			super(message);
			this.errorCode = ErrorCode.API_ERROR;
		}
	}
	
	/**
	 * Exception if problem with account access
	 */
	public static class AccountAccessException extends WeatherException {
		public AccountAccessException() {
			super();
			this.errorCode = ErrorCode.ACCESS_DENIED;
		}

		public AccountAccessException(String message) {
			super(message);
			this.errorCode = ErrorCode.ACCESS_DENIED;
		}
	}
}
