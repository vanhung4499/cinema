package com.hnv99.framework.shiro.realms;

import com.hnv99.common.utils.JwtUtil;
import com.hnv99.framework.shiro.JwtToken;
import com.hnv99.system.domain.SysUser;
import com.hnv99.system.service.impl.SysUserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Custom realm for authentication and authorization.
 */
@Slf4j
public class CustomerRealm extends AuthorizingRealm {

    @Autowired
    private SysUserServiceImpl sysUserService;

    /**
     * Override this method to avoid Shiro error.
     *
     * @param token The authentication token.
     * @return True if the token is an instance of JwtToken, false otherwise.
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    // Authorization
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    // Authentication
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String token = (String) authenticationToken.getCredentials();
        // Decrypt the token to get the username for database comparison
        String username = null;
        try {
            username = JwtUtil.getUsername(token);
        } catch (Exception e) {
            throw new AuthenticationException("Invalid token spelling or value is empty");
        }
        if (username == null) {
            log.error("Invalid token (empty or null)");
            throw new AuthenticationException("Invalid token");
        }
        SysUser user = sysUserService.findUserByName(username);
        if (user == null) {
            log.error("User does not exist");
            throw new AuthenticationException("User does not exist");
        }
        if (!JwtUtil.verify(token, username, user.getPassword())) {
            log.error("Incorrect username or password (invalid token or mismatch with login user)");
            throw new AuthenticationException("Incorrect username or password (invalid token or mismatch with login user)");
        }
        return new SimpleAuthenticationInfo(token, token, this.getName());
    }
}
