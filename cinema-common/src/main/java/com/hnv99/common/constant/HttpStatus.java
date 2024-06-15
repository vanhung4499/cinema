package com.hnv99.common.constant;

/**
 * Define constants to record HTTP status codes
 */
public class HttpStatus {
    /**
     * Success
     */
    public static final int SUCCESS = 200;

    public static final int BAD_REQUEST = 400;

    /**
     * Access restricted, authorization expired
     */
    public static final int FORBIDDEN = 403;

    /**
     * Resource or service not found
     */
    public static final int NOT_FOUND = 404;

    /**
     * Internal server error
     */
    public static final int ERROR = 500;
}
