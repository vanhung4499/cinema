package com.hnv99.common.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Utility class to retrieve request attributes from the current bound thread
 */
public class ServletUtil {

    /**
     * Get all request attributes
     *
     * @return Request attributes
     */
    public static ServletRequestAttributes getAttributes() {
        return (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    }

    /**
     * Get the request object
     *
     * @return HttpServletRequest object
     */
    public static HttpServletRequest getRequest() {
        return getAttributes().getRequest();
    }

    /**
     * Get the response object
     *
     * @return HttpServletResponse object
     */
    public static HttpServletResponse getResponse() {
        return getAttributes().getResponse();
    }

    /**
     * Get the session object
     *
     * @return HttpSession object
     */
    public static HttpSession getSession() {
        return getRequest().getSession();
    }

}
