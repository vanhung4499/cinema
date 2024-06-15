package com.hnv99.common.response;

import com.hnv99.common.constant.HttpStatus;

import java.util.HashMap;

import java.util.HashMap;

/**
 * Response message result, using Map storage
 */
public class ResponseResult extends HashMap<String, Object> {

    private static final long serialVersionUID = 1L;

    // Key for status code
    private static final String CODE = "code";

    // Key for operation result message
    private static final String MESSAGE = "msg";

    // Key for returned data
    private static final String DATA = "data";

    /**
     * Empty response message result
     */
    public ResponseResult() {
    }

    /**
     * Response message without data
     *
     * @param code Status code
     * @param msg  Response message
     */
    public ResponseResult(int code, String msg) {
        super.put(CODE, code);
        super.put(MESSAGE, msg);
    }

    /**
     * Construct response message
     *
     * @param code Status code
     * @param msg  Response message
     * @param data Response data
     */
    public ResponseResult(int code, String msg, Object data) {
        super.put(CODE, code);
        super.put(MESSAGE, msg);
        if (data != null) {
            super.put(DATA, data);
        }
    }

    /* Return success message */
    public static ResponseResult success() {
        return success("Operation succeeded");
    }

    public static ResponseResult success(String msg) {
        return success(msg, null);
    }

    public static ResponseResult success(Object data) {
        return success("Operation succeeded", data);
    }

    /**
     * Return success message
     *
     * @param msg  Response message
     * @param data Response data
     * @return Response message object
     */
    public static ResponseResult success(String msg, Object data) {
        return new ResponseResult(HttpStatus.SUCCESS, msg, data);
    }


    /* Return error message */
    public static ResponseResult error() {
        return error("Operation failed");
    }

    public static ResponseResult error(String msg) {
        return error(msg, null);
    }

    /**
     * Return error message with specified error status code
     *
     * @param code Error status code
     * @param msg  Error message
     * @return Response message object
     */
    public static ResponseResult error(int code, String msg) {
        return new ResponseResult(code, msg, null);
    }

    /**
     * Default error message type (500)
     *
     * @param msg  Error message
     * @param data Error data
     * @return Response message object
     */
    public static ResponseResult error(String msg, Object data) {
        return new ResponseResult(HttpStatus.ERROR, msg, data);
    }

}
