package com.hnv99.framework.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * Implementation of the AuthenticationToken interface of Shiro, JwtToken class.
 */
public class JwtToken implements AuthenticationToken {

    private String token;

    public JwtToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

}
