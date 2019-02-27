package com.mobiquityinc.packer;

/**
 * Exception class that serves for business exception cases.
 */
class APIException extends RuntimeException {
    APIException(String message) {
        super(message);
    }
}
