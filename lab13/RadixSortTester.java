import static org.junit.Assert.*;
import org.junit.Test;

public class RadixSortTester {


    @Test
    public void testRadixSort() {
        String[] students = {"Jason", "Anna", "Matt", "Jorge", "Mike"};
        String[] sortedStudents = RadixSort.sort(students);

        String[] expected = {"Anna", "Jason", "Jorge", "Matt", "Mike"};
        assertEquals(students.length, sortedStudents.length);
        for (int i = 0; i < expected.length; i += 1) {
            assertEquals(expected[i], sortedStudents[i]);
        }
    }
}
