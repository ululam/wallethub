package com.wallethub.entrancetask.az;

import java.util.*;

/**
 * @author alexey.zakharchenko@incryptex.com
 */
public final class Task2 {

    /**
     * Finds all the k-complementary pairs in the given array
     * @param array Input data
     * @param k Complementary factor
     *
     * @return Collection of all the k-complementary pairs found in the given array
     */
    public static Collection<IntPair> findKComplementary(int[] array, int k) {
        Objects.requireNonNull(array);

        if (array.length == 0) return Collections.emptyList();

        // Use set to avoid duplicates
        final Collection<IntPair> results = new HashSet<>();

        // array element -> count
        // @todo Possible its better to use large array with gaps here for performance ?
        final Map<Number, Integer> indexedArrayMap = new HashMap<>(array.length); // lets allocate memory for the worst scenario (no repeating numbers)
        // O(n)
        for (int element : array) {
            int count = indexedArrayMap.getOrDefault(element, 0);
            indexedArrayMap.put(element, ++count);
        }

        // O(n)
        for (int element : array) {
            int count = indexedArrayMap.getOrDefault( k - element, 0);
            if (count > 1 || (count == 1 && element+element != k)) { // Avoid adding same same element as complemantary pair to itself
                results.add(new IntPair(element, k - element));
            }
        }

        // = O(2n) = O(n)

        return results;
    }

    /**
     * Pair of integers
     */
    static final class IntPair {
        private final int x;
        private final int y;

        public IntPair(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            IntPair ip = (IntPair) o;

            return (x == ip.x && y == ip.y) || (x == ip.y && y == ip.x);
        }

        @Override
        public int hashCode() {
            return (x+y);
        }

        @Override
        public String toString() {
            return "[" + x + ',' + y + ']';
        }
    }
}
