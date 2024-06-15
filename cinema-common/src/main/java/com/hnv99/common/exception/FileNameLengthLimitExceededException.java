package com.hnv99.common.exception;

/**
 * Exception for exceeding the file name length limit, used for file validation
 */
public class FileNameLengthLimitExceededException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public FileNameLengthLimitExceededException() {
    }

    public FileNameLengthLimitExceededException(String message) {
        super(message);
    }

}
