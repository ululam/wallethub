package com.wallethub.entrancetask.az;

import org.apache.commons.lang.RandomStringUtils;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author alexey.zakharchenko@incryptex.com
 */
public class TestUtils {
    /**
     * Generates collection of random strings
     * @param capacity How many strings to geenrate
     *
     * @return collection of random strings
     */
    static Collection<String> generateStringCollection(int capacity) {
        Collection<String> res = new ArrayList<>(capacity);
        for (int i = 0; i < capacity; i++) {
            res.add(randomString());
        }

        return res;
    }

    private static final int MAX_INPUT_LENGTH = 1024;
    private static String randomString() {
        int length = Math.round((float)Math.random() * MAX_INPUT_LENGTH);

        return RandomStringUtils.random(length, true, true);
    }

}
