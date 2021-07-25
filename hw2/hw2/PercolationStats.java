package hw2;

import edu.princeton.cs.introcs.StdRandom;
import edu.princeton.cs.introcs.StdStats;

public class PercolationStats {

    private int grid;
    private int times;
    private int[] openSites;
    private double[] fractions;
    private PercolationFactory pf;

    /** Perform T independent experiments ona N-by-N grid.
     * @param N
     * @param T
     * @param pf
     */
    public PercolationStats(int N, int T, PercolationFactory pf) {
        if (N <= 0) {
            throw new IllegalArgumentException("N must be greater than zero.");
        }
        if (T <= 0) {
            throw new IllegalArgumentException("T must be greater than zero.");
        }
        grid = N;
        times = T;
        this.pf = pf;
        openSites = new int[times];
        fractions = new double[times];
        runSimulation();
    }

    /** Return sample mean of percolation threshold
     * @return
     */
    public double mean() {
        return StdStats.mean(fractions);
    }

    /** Return sample standard deviation of percolation threshold.
     * @return
     */
    public double stddev() {
        return StdStats.stddev(fractions);
    }

    /** Return low endpoint of 95% confidence interval.
     * @return
     */
    public double confidenceLow() {
        double mean = mean();
        double std = stddev();
        double confidenceLow = mean - 1.96 * std / Math.sqrt(times);
        return confidenceLow;
    }

    /** Return high endpoint of 95% confidence interval.
     * @return
     */
    public double confidenceHigh() {
        double mean = mean();
        double std = stddev();
        double confidenceHigh = mean + 1.96 * std / Math.sqrt(times);
        return confidenceHigh;
    }

    /** Repeat the computation experiment T times and store
     * the number of sites opened to openSites array.
     */
    private void runSimulation() {
        for (int t = 0; t < times; t += 1) {
            Percolation p = pf.make(grid);
            int i = 0;
            while (!p.percolates()) {
                openRandomSite(p);
                i += 1;
            }
            openSites[t] = i;
            fractions[t] = i * 1.0 / (grid * grid);
        }
    }

    /** Open one random site.
     * @param percolation
     */
    private void openRandomSite(Percolation percolation) {
        int row = StdRandom.uniform(grid);
        int col = StdRandom.uniform(grid);
        if (percolation.isOpen(row, col)) {
            openRandomSite(percolation);
        }
    }

}
