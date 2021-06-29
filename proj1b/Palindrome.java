public class Palindrome {

    /** Return a deque where the characters appear
     * in the same order as in the String. */
    public Deque<Character> wordToDeque(String word) {
        Deque<Character> d = new LinkedListDeque<>();
        if (word != null) {
            for (int i = 0; i < word.length(); i += 1) {
                d.addLast(word.charAt(i));
            }
        }
        return d;
    }

    /** Return true if the given word is a palindrome. */
    public boolean isPalindrome(String word) {
        Deque<Character> d = wordToDeque(word);
        while (d.size() > 1) {
            if (d.removeFirst() != d.removeLast()) {
                return false;
            }
        }
        return true;
    }

    /** Return true if the word is a palindrome according to
     * the character comparison test provided by the
     * CharacterComparator passed in as argument cc.
     *
     * To allow for odd length palindromes, we do not check
     * the middle character for equality with itself.
     */
    public boolean isPalindrome(String word, CharacterComparator cc) {
        Deque<Character> d = wordToDeque(word);
        while (d.size() > 1) {
            return cc.equalChars(d.removeFirst(), d.removeLast());
        }
        return true;
    }
}

