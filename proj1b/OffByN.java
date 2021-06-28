public class OffByN implements CharacterComparator {

    private int offByN;

    /** Constructor */
    public OffByN(int N) {
        offByN = N;
    }

    /** Returns true for characters that are different by N. */
    @Override
    public boolean equalChars(char x, char y) {
        if (y - x == offByN || x - y == offByN) {
            return true;
        }
        return false;
    }
}
