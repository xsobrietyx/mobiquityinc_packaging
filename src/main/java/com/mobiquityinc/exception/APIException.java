package com.mobiquityinc.exception;

/**
 * Exception class that serves for business exception cases.
 */
public class APIException extends RuntimeException {
    public APIException(String message) {
        super("API exception: " + message);
    }
}
