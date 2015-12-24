package com.wallethub.entrancetask.az;

import com.sun.javafx.binding.StringFormatter;

import java.util.Collection;

/**
 * @author alexey.zakharchenko@incryptex.com
 */
class Utils {
    public static final String LINE_END = System.getProperty("line.separator");
    public static final String USER_HOME = System.getProperty("user.home");

    /**
     * Writes message to output (console or log)
     * @param message Message string
     */
    static void out(String message) {
        // To avoid extra dependencies which adds no value to the project I use std output not logging
        System.out.println(message);
    }

    static void out(String message, Object... args) {
        out(StringFormatter.format(message, args).getValue());
    }

    /**
     * Shortcut to System.currentTimeMillis();
     * @return Current time ms
     */
    static long t() {
        return System.currentTimeMillis();
    }

    public static String s(Collection c) {
        if (c == null || c.isEmpty()) return "[]";

        StringBuilder buf = new StringBuilder("[");
        for (Object o : c)
            buf.append(String.valueOf(o)).append(", ");

        return buf.substring(0, buf.length()-2) + "]";
    }

    public static String s(int[] array) {
        if (array == null) return "null";
        if (array.length == 0) return "[]";

        StringBuilder buf = new StringBuilder("[");
        for (Object o : array) buf.append(String.valueOf(o)).append(", ");

        return buf.substring(0, buf.length()-2) + "]";
    }

}
