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
        assertTrue(palindrome.isPalindrome("racecar"));
        assertFalse(palindrome.isPalindrome("car"));
        palindrome.wordToDeque(null);
        assertTrue(palindrome.isPalindrome(null));
        assertTrue(palindrome.isPalindrome("a"));

        assertTrue(palindrome.isPalindrome("abcdedcba"));
        assertTrue(palindrome.isPalindrome("abba"));
        assertFalse(palindrome.isPalindrome("horse"));
    }

    @Test
    public void testIsPalindromeOffByOne() {

        CharacterComparator offByOne = new OffByOne();

        assertTrue(palindrome.isPalindrome("flake", offByOne));
        assertTrue(palindrome.isPalindrome("tutu", offByOne));
        assertTrue(palindrome.isPalindrome("tress", offByOne));
        assertTrue(palindrome.isPalindrome("ungot", offByOne));
        assertTrue(palindrome.isPalindrome("chrysid", offByOne));
        assertTrue(palindrome.isPalindrome("clamb", offByOne));
        assertFalse(palindrome.isPalindrome("abcba", offByOne));
        assertTrue(palindrome.isPalindrome(null, offByOne));
        assertTrue(palindrome.isPalindrome("a", offByOne));

    }


    @Test
    public void testIsPalindromeOffByN() {

        CharacterComparator ob5 = new OffByN(5);

        assertTrue(palindrome.isPalindrome("tiny", ob5));
        assertTrue(palindrome.isPalindrome("totty", ob5));
        assertFalse(palindrome.isPalindrome("abcba", ob5));
        assertTrue(palindrome.isPalindrome(null, ob5));
        assertTrue(palindrome.isPalindrome("a", ob5));

    }
}

