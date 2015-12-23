package com.wallethub.entrancetask.az;

import com.wallethub.entrancetask.az.Task2.IntPair;
import org.testng.annotations.Test;

import java.util.*;

import static com.wallethub.entrancetask.az.Task2.findKComplementary;
import static com.wallethub.entrancetask.az.TestUtils.eq;
import static com.wallethub.entrancetask.az.Utils.*;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.testng.Assert.*;

/**
 * @author alexey.zakharchenko@incryptex.com
 */
public class Task2Test {

    @Test
    public void testFindKComplementary() throws Exception {
        for (TestData data : testData()) {
            Collection<IntPair> pairs = findKComplementary(data.array, data.k);
            assertTrue(eq(pairs, data.results), "Was " + s(pairs) + ", is " + data);
        }
    }

    @Test
    public void testIt() {
        System.out.println(MAX_VALUE + MIN_VALUE);
        System.out.println(MAX_VALUE + 100);
        System.out.println((MIN_VALUE - 100));
        System.out.println((MAX_VALUE + MIN_VALUE) == -1);
        System.out.println(MAX_VALUE + 100 + MIN_VALUE);
        System.out.println(MAX_VALUE + 100 + MIN_VALUE == 99);
    }

    private List<TestData> testData() {
        List<TestData> data = new ArrayList<>();

        data.add(td(new int[0], 0, emptyList()));
        data.add(td(new int[1], -1, emptyList()));
        data.add(td(new int[127], -1, emptyList()));

        data.add(td(new int[] {1,2}, 0, emptyList()));
        data.add(td(new int[] {1,2,3,4}, 4, asList(p(1,3))));
        data.add(td(new int[] {1,2,0,2}, 4, asList(p(2,2))));
        data.add(td(new int[] {1,4,1,4}, 5, asList(p(1,4))));
        data.add(td(new int[] {1,4,2,3}, 5, asList(p(1,4), p(2,3))));
        data.add(td(new int[] {-1,0,1,0}, 0, asList(p(-1,1), p(0,0))));
        data.add(td(new int[] {MAX_VALUE-1, MIN_VALUE+1}, 8, emptyList()));
        data.add(td(new int[] {MAX_VALUE-1, MIN_VALUE+1}, -8, emptyList()));
        data.add(td(new int[] {MAX_VALUE, MIN_VALUE}, -1, asList(p(MAX_VALUE, MIN_VALUE))));
        data.add(td(new int[] {MAX_VALUE, MIN_VALUE+1}, 0, asList(p(MAX_VALUE, MIN_VALUE+1))));

        return data;
    }

    // Syntax sugar
    private static TestData td(int[] array, int k, Collection<IntPair> results) {
        return new TestData(array, k, results);
    }


    // Syntax sugar
    private static IntPair p(int i, int j) {
        return new IntPair(i,j);
    }

    private static class TestData {
        private static int counter = 1;
        private final int[] array;
        private final int k;
        private final Collection<IntPair> results;
        private final int index;

        public TestData(int[] array, int k, Collection<IntPair> results) {
            this.array = array;
            this.k = k;
            this.results = results;
            index = counter++;
        }

        @Override
        public String toString() {
            return String.format("TestData #%s {%s, %s, %s}", index, s(array), k, s(results));
        }
    }

}