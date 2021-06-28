import org.junit.Test;
import static org.junit.Assert.*;

public class TestOffByN {

    @Test
    public void testOffByN() {
        CharacterComparator ob5 = new OffByN(5);
        assertTrue(ob5.equalChars('a', 'f'));
        assertTrue(ob5.equalChars('f', 'a'));
        assertFalse(ob5.equalChars('f', 'h'));
        assertFalse(ob5.equalChars('a', 'a'));

       CharacterComparator ob0 = new OffByN(0);
       assertFalse(ob0.equalChars('a', 'f'));
       assertTrue(ob0.equalChars('a', 'a'));

    }

}
