package com.hnv99.common.utils;

/**
 * String utility class, containing commonly used operations
 */
public class StringUtil {

    /**
     * Check if val is not empty (not null or "")
     *
     * @param val The value to check
     * @return True if val is not empty, false otherwise
     */
    public static boolean isNotEmpty(String val) {
        return val != null && !val.isEmpty();
    }

    /**
     * Convert the given camel case value to underscore case
     *
     * @param val The camel case value
     * @return The underscore case value
     */
    public static String toUnderScoreCase(String val) {
        if (!isNotEmpty(val)) {
            return val;
        }
        StringBuilder sb = new StringBuilder(val);
        for (int i = 0; i < sb.length(); i++) {
            if (sb.charAt(i) >= 'A' && sb.charAt(i) <= 'Z') {
                // Replace uppercase letter "A" with "_a"
                sb.replace(i, i + 1, "_" + (char) (sb.charAt(i) + 32));
            }
        }
        return sb.toString();
    }

}
