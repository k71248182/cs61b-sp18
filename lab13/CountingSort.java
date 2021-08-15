/**
 * Class with 2 ways of doing Counting sort, one naive way and one "better" way
 *
 * @author Akhil Batra, Alexander Hwang
 *
 **/
public class CountingSort {
    /**
     * Counting sort on the given int array. Returns a sorted version of the array.
     * Does not touch original array (non-destructive method).
     * DISCLAIMER: this method does not always work, find a case where it fails
     *
     * @param arr int array that will be sorted
     * @return the sorted array
     */
    public static int[] naiveCountingSort(int[] arr) {
        // find max
        int max = Integer.MIN_VALUE;
        for (int i : arr) {
            max = max > i ? max : i;
        }

        // gather all the counts for each value
        int[] counts = new int[max + 1];
        for (int i : arr) {
            counts[i]++;
        }

        // when we're dealing with ints, we can just put each value
        // count number of times into the new array
        int[] sorted = new int[arr.length];
        int k = 0;
        for (int i = 0; i < counts.length; i += 1) {
            for (int j = 0; j < counts[i]; j += 1, k += 1) {
                sorted[k] = i;
            }
        }

        // however, below is a more proper, generalized implementation of
        // counting sort that uses start position calculation
        int[] starts = new int[max + 1];
        int pos = 0;
        for (int i = 0; i < starts.length; i += 1) {
            starts[i] = pos;
            pos += counts[i];
        }

        int[] sorted2 = new int[arr.length];
        for (int i = 0; i < arr.length; i += 1) {
            int item = arr[i];
            int place = starts[item];
            sorted2[place] = item;
            starts[item] += 1;
        }

        // return the sorted array
        return sorted;
    }

    /**
     * Counting sort on the given int array, must work even with negative numbers.
     * Note, this code does not need to work for ranges of numbers greater
     * than 2 billion.
     * Does not touch original array (non-destructive method).
     *
     * @param arr int array that will be sorted
     */
    public static int[] betterCountingSort(int[] arr) {
        // Find min and max. Min is capped at zero, and Max is floored at zero.
        int max = 0;
        int min = 0;
        for (int i : arr) {
            max = max > i ? max : i;
            min = min < i ? min : i;
        }

        /** Gather all the counts for each value.
         * Store non-negative and negative buckets into two separate arrays.
         */
        int[] countNonNegative = new int[max + 1];
        int[] countNegative = new int[-min + 1];
        for (int i : arr) {
            if (i >= 0) {
                countNonNegative[i]++;
            } else {
                countNegative[-i]++;
            }
        }

        /** Values of start represent the first position in the sorted array for
         * that bucket.
         */
        int[] startNonNegative = new int[max + 1];
        int[] startNegative = new int[-min + 1];
        int pos = 0;
        for (int i = min; i < 0; i += 1) {
            startNegative[-i] = pos;
            pos += countNegative[-i];
        }
        for (int i = 0; i <= max ; i += 1) {
            startNonNegative[i] = pos;
            pos += countNonNegative[i];
        }

        int[] sorted = new int[arr.length];
        for (int i = 0; i < arr.length; i += 1) {
            int item = arr[i];
            if (item >= 0) {
                int place = startNonNegative[item];
                sorted[place] = item;
                startNonNegative[item] += 1;
            } else {
                int place = startNegative[-item];
                sorted[place] = item;
                startNegative[-item] += 1;
            }
        }
        return sorted;
    }
}
