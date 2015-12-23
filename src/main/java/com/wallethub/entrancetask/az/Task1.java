package com.wallethub.entrancetask.az;

/**
 * String is a palindrome if it matches reverse string, case-sensitive
 *
 * @author alexey.zakharchenko@incryptex.com
 */
public final class Task1 {
    /**
     * Reuse java built-in functionality - just for comparison (@see Task1Test for details)
     *
     * @param s String
     *
     * @return true if the given string is a palindrome
     */
    public static boolean isPalindrome(String s) {
        if (!commonBoundariesCheck(s))
            return false;

        String reverse = new StringBuffer(s).reverse().toString();

        return s.equals(reverse);
    }

    /**
     * Method [almost] doesn't allocate any new objects here, but reusing inner String char array.
     * Is much faster than isPalindrome() for non-palindrome strings (we have no need to inverse all the array 'cause
     *  we encounter chars that are different much faster than we reach the last char)
     *
     * @param s String
     *
     * @return true if the given string is a palindrome
     */
    public static boolean isPalindromeEfficient(String s) {
        if (!commonBoundariesCheck(s))
            return false;

        int length = s.length();
        int halfLength = length >> 1;
        //  In fact, if we call s.toCharArray() and iterate over it, it works slower than code below
        for (int i = 0; i < halfLength; i++) {
            if (s.charAt(i) != s.charAt(length-i-1))
                return false;
        }

        return true;
    }


    // Instead of using extra libs such as apache commons, I use this simple method
    static boolean commonBoundariesCheck(String s) {
        // I don't use trim - I treat \\s+ as meaningful characters
        return s != null && s.length() > 0;
    }
}
