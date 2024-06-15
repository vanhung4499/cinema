package com.hnv99.common.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * Token signing and verification utility class
 */
@Slf4j
public class JwtUtil {
    /**
     * JWT expiration time EXPIRE_TIME in minutes, currently set to one hour
     */
    private static final long EXPIRE_TIME = 60 * 60 * 24 * 7 * 1000; // 7 day

    /**
     * Verify if the token is correct
     *
     * @param token   The token
     * @param username The user's username
     * @param secret   The user's password
     * @return Whether it is correct
     */
    public static boolean verify(String token, String username, String secret) {
        try {
            // Generate JWT verifier based on password
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim("username", username)
                    .build();
            // Verify the TOKEN
            DecodedJWT jwt = verifier.verify(token);
            log.info("Login verification succeeded!");
            return true;
        } catch (Exception exception) {
            log.error("JwtUtil login verification failed!");
            return false;
        }
    }

    /**
     * Get information from the token without secret decryption
     *
     * @param token The token
     * @return The username contained in the token
     */
    public static String getUsername(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("username").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * Generate a token signature that expires after EXPIRE_TIME minutes
     *
     * @param username The username
     * @param secret   The user's password
     * @return Encrypted token
     */
    public static String sign(String username, String secret) {
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        Algorithm algorithm = Algorithm.HMAC256(secret);
        // Attach username information
        return JWT.create()
                .withClaim("username", username)
                .withExpiresAt(date)
                .sign(algorithm);

    }

    public static void main(String[] args) {
        /**
         * Test to generate a token
         * Result: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2MDc0ODI5OTYsInVzZXJuYW1lIjoi5ZGo5YWrIn0.UP6kFC0BofuX7FLoPDMWCQno-NhVuYA0NlQG8xgt2Rc
         */
        String sign = sign("Zhou Ba", "f93643c0eacc54a5ee1783744466ab9e");
        log.warn("Test to generate a token\n" + sign);
    }
}

