package com.hnv99.framework.config;

import com.hnv99.framework.shiro.JwtFilter;
import com.hnv99.framework.shiro.realms.CustomerRealm;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Configuration for the Shiro security framework.
 */
@Configuration
public class ShiroConfig {

    // Create the Shiro filter
    @Bean("shiroFilter")
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(DefaultWebSecurityManager defaultWebSecurityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

        // Set the security manager for the Shiro filter
        shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager);

        // Configure restricted resources
        Map<String, String> map = new LinkedHashMap<>();
        // Allow registration and login
        map.put("/sysUser/register", "anon");
        map.put("/sysUser/login", "anon");
        // Allow image queries
        map.put("/images/**", "anon");
        // Requests for this resource require authentication and authorization
        map.put("/sysCinema/update", "jwt");
        // Allow cinema query requests
        map.put("/sysCinema/**", "anon");
        // Allow movie-related search requests
        map.put("/sysMovie/find/**", "anon");
        // Allow movie category-related search requests
        map.put("/sysMovieCategory/find/**", "anon");
        // Allow movie session-related search requests
        map.put("/sysSession/find/**", "anon");
        // Requests for this resource require authentication and authorization
        map.put("/**", "jwt");

        // Add custom filters and name it 'jwt'
        Map<String, Filter> filterMap = new HashMap<String, Filter>(1);
        filterMap.put("jwt", new JwtFilter());
        shiroFilterFactoryBean.setFilters(filterMap);

        // Set authentication rules
        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);
        return shiroFilterFactoryBean;
    }

    // Create the security manager
    @Bean
    public DefaultWebSecurityManager getDefaultWebSecurityManager(Realm realm) {
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();

        // Set the realm for the security manager
        defaultWebSecurityManager.setRealm(realm);

        // Disable Shiro's default session to not save login state and use token for each verification
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        defaultWebSecurityManager.setSubjectDAO(subjectDAO);

        return defaultWebSecurityManager;
    }

    // Create a custom realm
    @Bean(name = "realm")
    public Realm getRealm() {
        CustomerRealm realm = new CustomerRealm();
        return realm;
    }

}
