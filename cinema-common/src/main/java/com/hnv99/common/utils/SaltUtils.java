package com.hnv99.common.utils;

import java.util.Random;

import java.util.Random;

/**
 * Utility class to generate random salts
 */
public class SaltUtils {

    public static String getSalt(int n) {
        // Return a random salt of length n
        StringBuilder sb = new StringBuilder();
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()".toCharArray();
        for (int i = 0; i < n; i++) {
            char c = chars[new Random().nextInt(chars.length)];
            sb.append(c);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(getSalt(8));
    }

}
