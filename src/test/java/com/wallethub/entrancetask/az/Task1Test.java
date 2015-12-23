package com.wallethub.entrancetask.az;

import org.testng.annotations.Test;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static com.wallethub.entrancetask.az.Task1.commonBoundariesCheck;
import static com.wallethub.entrancetask.az.Task1.isPalindrome;
import static com.wallethub.entrancetask.az.Task1.isPalindromeEfficient;
import static com.wallethub.entrancetask.az.TestUtils.generateStringCollection;
import static com.wallethub.entrancetask.az.Utils.out;
import static com.wallethub.entrancetask.az.Utils.t;
import static java.lang.String.valueOf;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * @author alexey.zakharchenko@incryptex.com
 */
public class Task1Test {
    private static final Map<String, Boolean> testData = new HashMap<>(8);
    static {
        testData.put(null, false);
        testData.put("", false);
        testData.put("1", true);
        testData.put("AA", true);
        testData.put("A1", false);
        testData.put("AA ", false);
        testData.put(" BB ", true);
        testData.put(" BB", false);
        testData.put("ABA", true);
        testData.put("ABa", false);
        testData.put("aBAb", false);
        testData.put("a roza upala na lapu azora", false);
        testData.put("arozaupalanalapuazora", true);
    }

    @Test
    public void testCommonBoundariesCheck() {
        assertFalse(commonBoundariesCheck(null));
        assertFalse(commonBoundariesCheck(""));
        assertTrue(commonBoundariesCheck(" "));
        assertTrue(commonBoundariesCheck("1"));
    }

    @Test
    public void testIsPalindrome() throws Exception {
        for (Map.Entry<String, Boolean> datum : testData.entrySet()) {
            assertTrue(isPalindrome(datum.getKey()) == datum.getValue(), valueOf(datum));
        }
    }

    @Test
    public void testIsPalindromeEfficient() throws Exception {
        for (Map.Entry<String, Boolean> datum : testData.entrySet()) {
            assertTrue(isPalindromeEfficient(datum.getKey()) == datum.getValue(), valueOf(datum));
        }
    }

    // Does not test but provide comparison information
    @Test
    public void testComparison() throws Exception {
        final Collection<String> input = generateStringCollection(500_000);
        out("Testing with number of strings: " + input.size());

        long start = t();
        input.forEach(Task1::isPalindromeEfficient);
        out("isPalindromeEfficient() took " + (t() - start));

        start = t();
        input.forEach(Task1::isPalindrome);
        out("isPalindrome() took " + (t() - start));
    }

}