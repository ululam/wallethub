package com.wallethub.entrancetask.az;

/**
 * String is a palindrome if it matches reverse string, case-sensitive
 *
 * @author alexey.zakharchenko@incryptex.com
 */
public class Task1 {
    // Reuse java built-in functionality
    public static boolean isPalindrome(String s) {
        if (!commonBoundariesCheck(s))
            return false;

        String reverse = new StringBuffer(s).reverse().toString();

        return s.equals(reverse);
    }

    // Method [almost] doesn't allocate any new objects here, but reusing inner String char array
    public static boolean isPalindromeEfficient(String s) {
        if (!commonBoundariesCheck(s))
            return false;

        int length = s.length();
        int halfLength = length >> 1;
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
