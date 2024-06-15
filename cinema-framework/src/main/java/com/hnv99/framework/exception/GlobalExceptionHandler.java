package com.hnv99.framework.exception;

import com.hnv99.common.constant.HttpStatus;
import com.hnv99.common.exception.DataNotFoundException;
import com.hnv99.common.response.ResponseResult;
import org.apache.shiro.authc.AuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * Configuration of the exception handler to manage global exceptions.
 */
@RestController
@ControllerAdvice
public class GlobalExceptionHandler {

    // Logger for displaying error messages on the console
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handle method argument not valid exceptions and respond with error messages to the frontend.
     *
     * @return Error response information
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseResult methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        // Log the error
        log.error(e.getMessage(), e);
        // Get the result of this exception
        BindingResult bindingResult = e.getBindingResult();
        // Get the error message
        String msg = bindingResult.getAllErrors().get(0).getDefaultMessage();
        return ResponseResult.error(msg);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseResult sqlIntegrityConstraintViolationExceptionHandler(SQLIntegrityConstraintViolationException e) {
        log.error(e.getMessage(), e);
        return ResponseResult.error("Illegal insert or modify operation");
    }

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseResult dataNotFoundExceptionHandler(DataNotFoundException e) {
        log.warn(e.getMessage());
        return ResponseResult.error(e.getMessage());
    }

    @ExceptionHandler(NoSuchMethodException.class)
    public ResponseResult noSuchMethodExceptionHandler(NoSuchMethodException e) {
        log.warn(e.getMessage());
        return ResponseResult.error("Sorry, there was an internal server error");
    }

    @ExceptionHandler(IllegalAccessException.class)
    public ResponseResult illegalAccessExceptionHandler(IllegalAccessException e) {
        log.warn(e.getMessage());
        return ResponseResult.error("Sorry, there was an internal server error");
    }

    @ExceptionHandler(IOException.class)
    public ResponseResult IOExceptionHandler(IOException e) {
        log.warn(e.getMessage());
        return ResponseResult.error("File information error, reason: " + e.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseResult authenticationExceptionHandler(AuthenticationException e) {
        log.warn(e.getMessage());
        return ResponseResult.error(HttpStatus.BAD_REQUEST, e.getMessage());
    }

}
