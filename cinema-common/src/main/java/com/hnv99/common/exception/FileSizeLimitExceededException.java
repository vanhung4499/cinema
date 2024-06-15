package com.hnv99.common.exception;

/**
 * Exception for exceeding the file size limit, used for file validation
 */
public class FileSizeLimitExceededException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public FileSizeLimitExceededException() {
    }

    public FileSizeLimitExceededException(String message) {
        super(message);
    }

}
