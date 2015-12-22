package com.wallethub.entrancetask.az;

/**
 * @author alexey.zakharchenko@incryptex.com
 */
class Utils {
    /**
     * Writes message to output (console or log)
     * @param message Message string
     */
    static void out(String message) {
        // To avoid extra dependencies which adds no value to the project I use std output not logging
        System.out.println(message);
    }

    /**
     * Shortcut to System.currentTimeMillis();
     * @return Current time ms
     */
    static long t() {
        return System.currentTimeMillis();
    }
}
