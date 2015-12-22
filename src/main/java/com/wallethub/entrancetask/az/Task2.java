package com.wallethub.entrancetask.az;

import java.util.Collection;
import java.util.Objects;

/**
 * @author alexey.zakharchenko@incryptex.com
 */
public class Task2 {

    /**
     * Finds all the k-complementary pairs in the given array
     * @param array Input data
     * @param k Complementary factor
     *
     * @return Collection of all the k-complementary pairs found in the given array
     */
    public Collection<IntPair> findKComplementary(int[] array, int k) {
        Objects.requireNonNull(array);

        return null;
    }

    /**
     * Pair of integers
     */
    static class IntPair extends Tuple<Integer,Integer> {
        public IntPair(Integer x, Integer y) {
            super(x,y);
        }
    }
}
