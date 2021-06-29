import org.junit.Test;
import static org.junit.Assert.*;

public class TestPalindrome {
    /* You must use this palindrome, and not instantiate
     new Palindromes, or the autograder might be upset. */
    static Palindrome palindrome = new Palindrome();

    @Test
    public void testWordToDeque() {
        Deque d = palindrome.wordToDeque("persiflage");
        String actual = "";
        for (int i = 0; i < "persiflage".length(); i++) {
            actual += d.removeFirst();
        }
        assertEquals("persiflage", actual);
    }

    @Test
    public void testIsPalindrome() {

        // test corner case
        assertTrue(palindrome.isPalindrome(null));
        assertTrue(palindrome.isPalindrome("a"));

        // test alphabetical and non-alphabetical characters
        assertTrue(palindrome.isPalindrome("racecar"));
        assertTrue(palindrome.isPalindrome("#%$%#"));
        assertTrue(palindrome.isPalindrome("1234321"));
        assertTrue(palindrome.isPalindrome("abba"));
        assertTrue(palindrome.isPalindrome("#%%#"));
        assertTrue(palindrome.isPalindrome("89044098"));

        assertFalse(palindrome.isPalindrome("car"));
        assertFalse(palindrome.isPalindrome("##*^^"));
        assertFalse(palindrome.isPalindrome("4321"));
        assertFalse(palindrome.isPalindrome("abca"));
        assertFalse(palindrome.isPalindrome("%%$$&&"));
        assertFalse(palindrome.isPalindrome("123312"));

        // test upper and lower cases
        assertFalse(palindrome.isPalindrome("ABba"));
        assertFalse(palindrome.isPalindrome("ABvba"));
    }

    @Test
    public void testIsPalindromeOffByOne() {

        CharacterComparator offByOne = new OffByOne();

        // test corner case
        assertTrue(palindrome.isPalindrome(null, offByOne));
        assertTrue(palindrome.isPalindrome("a", offByOne));

        // test alphabetical and non-alphabetical characters
        assertTrue(palindrome.isPalindrome("flake", offByOne));
        assertTrue(palindrome.isPalindrome("tutu", offByOne));
        assertTrue(palindrome.isPalindrome("TresS", offByOne));
        assertTrue(palindrome.isPalindrome("ungot", offByOne));
        assertTrue(palindrome.isPalindrome("chrysid", offByOne));
        assertTrue(palindrome.isPalindrome("1234412", offByOne));
        assertTrue(palindrome.isPalindrome("123412", offByOne));
        assertTrue(palindrome.isPalindrome("&%&%", offByOne));
        assertTrue(palindrome.isPalindrome("&3%a&2%", offByOne));

        assertFalse(palindrome.isPalindrome("cba", offByOne));
        assertFalse(palindrome.isPalindrome("3215", offByOne));
        assertFalse(palindrome.isPalindrome("$&o#&", offByOne));
        assertFalse(palindrome.isPalindrome("Flake", offByOne));
    }

    @Test
    public void testIsPalindromeOffByN() {

        // Test when N = 5 & N = -1
        CharacterComparator ob5 = new OffByN(5);
        CharacterComparator offByOne = new OffByN(-1);

        // corner case
        assertTrue(palindrome.isPalindrome(null, ob5));
        assertTrue(palindrome.isPalindrome("a", ob5));

        assertTrue(palindrome.isPalindrome(null, offByOne));
        assertTrue(palindrome.isPalindrome("a", offByOne));

        // Test when N = 5
        assertTrue(palindrome.isPalindrome("tiny", ob5));
        assertTrue(palindrome.isPalindrome("totty", ob5));
        assertFalse(palindrome.isPalindrome("abcba", ob5));


        // Test when N = -1
        assertTrue(palindrome.isPalindrome("flake", offByOne));
        assertTrue(palindrome.isPalindrome("tutu", offByOne));
        assertTrue(palindrome.isPalindrome("TresS", offByOne));
        assertTrue(palindrome.isPalindrome("ungot", offByOne));
        assertTrue(palindrome.isPalindrome("chrysid", offByOne));
        assertTrue(palindrome.isPalindrome("1234412", offByOne));
        assertTrue(palindrome.isPalindrome("123412", offByOne));
        assertTrue(palindrome.isPalindrome("&%&%", offByOne));
        assertTrue(palindrome.isPalindrome("&3%a&2%", offByOne));

        assertFalse(palindrome.isPalindrome("cba", offByOne));
        assertFalse(palindrome.isPalindrome("3215", offByOne));
        assertFalse(palindrome.isPalindrome("$&o#&", offByOne));
        assertFalse(palindrome.isPalindrome("Flake", offByOne));


    }
}

