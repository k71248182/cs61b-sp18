package hw3.hash;

import java.util.List;

public class OomageTestUtility {

    /** Returns true if the given oomages have hashCodes that would
     * distribute them fairly evenly across M buckets.
     * To do this, convert each oomage's hashcode in the
     * same way as in the visualizer, i.e. (& 0x7FFFFFFF) % M.
     * and ensure that no bucket has fewer than N / 50
     * Oomages and no bucket has more than N / 2.5 Oomages. */
    public static boolean haveNiceHashCodeSpread(List<Oomage> oomages, int M) {

        int[] countEachBucket = new int[M];
        for (Oomage o : oomages) {
            int bucketNum = (o.hashCode() & 0x7FFFFFFF) % M;
            countEachBucket[bucketNum] += 1;
        }

        int zeroOomages = 0;
        int exceedMasOomages = 0;
        double maxNumOomages = oomages.size() * 1.0 / 2.5;
        for (int i = 0; i < M; i += 1) {
            if (countEachBucket[i] == 0) {
                return false;
            } else if (countEachBucket[i] > maxNumOomages) {
                return false;
            }
        }
        return true;
    }
}
