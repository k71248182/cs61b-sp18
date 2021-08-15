/**
 * Class for doing Radix sort
 *
 * @author Akhil Batra, Alexander Hwang
 *
 */
public class RadixSort {
    /**
     * Does LSD radix sort on the passed in array with the following restrictions:
     * The array can only have ASCII Strings (sequence of 1 byte characters)
     * The sorting is stable and non-destructive
     * The Strings can be variable length (all Strings are not constrained to 1 length)
     *
     * @param asciis String[] that needs to be sorted
     *
     * @return String[] the sorted array
     */
    public static String[] sort(String[] asciis) {

        // Find the length of the longest string.
        int max = 0;
        for (String s : asciis) {
            int l = s.length();
            max = max > l ? max : l;
        }

        String[] sorted = asciis.clone();
        for (int index = max - 1; index >= 0; index -= 1) {
            sortHelperLSD(sorted, index);
        }

        return sorted;
    }

    /**
     * LSD helper method that performs a destructive counting sort the array of
     * Strings based off characters at a specific index.
     * @param asciis Input array of Strings
     * @param index The position to sort the Strings on (staring from 0).
     */
    private static void sortHelperLSD(String[] asciis, int index) {
        int counts[] = new int[256];
        int placeholder = 0;         // number of shorter strings
        for (String s : asciis) {
            if (s.length() < index + 1) {
                placeholder += 1;
            } else {
                int i = s.charAt(index);
                counts[i] += 1;
            }
        }

        int[] starts = new int[256];
        int pos = placeholder;
        for (int i = 0; i < starts.length; i += 1) {
            starts[i] = pos;
            pos += counts[i];
        }

        String [] unSorted = asciis.clone();
        placeholder = 0;
        for (String s : unSorted) {
            if (s.length() < index + 1) {
                asciis[placeholder] = s;
                placeholder += 1;
            } else {
                int i = s.charAt(index);
                int place = starts[i];
                asciis[place] = s;
                starts[i] += 1;
            }
        }
    }

    /**
     * MSD radix sort helper function that recursively calls itself to achieve the sorted array.
     * Destructive method that changes the passed in array, asciis.
     *
     * @param asciis String[] to be sorted
     * @param start int for where to start sorting in this method (includes String at start)
     * @param end int for where to end sorting in this method (does not include String at end)
     * @param index the index of the character the method is currently sorting on
     *
     **/
    private static void sortHelperMSD(String[] asciis, int start, int end, int index) {
        // Optional MSD helper method for optional MSD radix sort
        return;
    }
}
