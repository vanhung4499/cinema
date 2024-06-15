package com.hnv99.common.exception;

/**
 * Exception for invalid file extension, used for file validation
 */
public class InvalidExtensionException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InvalidExtensionException() {
    }

    public InvalidExtensionException(String message) {
        super(message);
    }

}
