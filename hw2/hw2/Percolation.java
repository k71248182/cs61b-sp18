package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private int maxGrid;
    private boolean[][] openSites;
    private boolean percolate;
    private int numberOfOpenSites;
    private int top;                        // virtual top tile
    private int bottom;                     // virtual bottom tile
    private WeightedQuickUnionUF ufTop;     // connection to the virtual top
    private WeightedQuickUnionUF ufBottom;  // connection to the virtual bottom

    /** Constructor: create N-by-N grid,
     * with all sites initially blocked.
     * @param N
     */
    public Percolation(int N) {
        maxGrid = N;
        openSites = new boolean[N][N];
        for (int i = 0; i < N; i += 1) {
            for (int j = 0; j < N; j += 1) {
                openSites[i][j] = false;
            }
        }
        numberOfOpenSites = 0;
        percolate = false;
        int maxUF = xyTo1D(maxGrid - 1, maxGrid - 1);
        ufTop = new WeightedQuickUnionUF(maxUF + 3);
        ufBottom = new WeightedQuickUnionUF(maxUF + 3);
        top = maxUF + 1;
        bottom = maxUF + 2;
    }

    /** Open the site (row, col) if it is not open already.
     * @param row
     * @param col
     */
    public void open(int row, int col) {
        if (!isOpen(row, col)) {
            openSites[row][col] = true;
            numberOfOpenSites += 1;
            updateConnections(row, col);
        }
    }

    /** Return true if the site (row, col) is open.
     * @param row
     * @param col
     * @return
     */
    public boolean isOpen(int row, int col) {
        checkRange(row, col);
        return openSites[row][col];
    }

    /** Return true if the site (row, col) is full.
     * @param row
     * @param col
     * @return
     */
    public boolean isFull(int row, int col) {
        checkRange(row, col);
        int site = xyTo1D(row, col);
        return ufTop.connected(site, top);
    }

    /** Return number of open sites.
     * @return
     */
    public int numberOfOpenSites() {
        return numberOfOpenSites;
    }

    /** Return true if the system percolates.
     * @return
     */
    public boolean percolates() {
        return percolate;
    }

    /** Used for testing */
    public static void main(String[] args) {

    }

    /** Check corner cases. Throw a exception if the row
     * or column index is out of boundary.
     * @param row
     * @param col
     */
    private void checkRange(int row, int col) {
        if (row < 0) {
            throw new IllegalArgumentException("negative row number.");
        }
        if (col < 0) {
            throw new IllegalArgumentException("negative column number.");
        }
        if (row >= maxGrid) {
            throw new IndexOutOfBoundsException("row number out of boundary.");
        }
        if (col >= maxGrid) {
            throw new IndexOutOfBoundsException("column number out of boundary.");
        }
    }

    /** Convert position (row & column index) to 1D int.
     * @param row
     * @param col
     * @return
     */
    private int xyTo1D(int row, int col) {
        int p = row * maxGrid + col;
        return p;
    }

    /** Update connections when one site is opened. */
    private void updateConnections(int row, int col) {
        int newOpenSite = xyTo1D(row, col);

        // handle tile on the first row
        if (row == 0) {
            ufTop.union(newOpenSite, top);
        }

        // handle tile on the bottom row
        if (row == maxGrid - 1) {
            ufBottom.union(newOpenSite, bottom);
        }

        // update connections to adjacent cells.
        if (row - 1 >= 0) {
            updateConnections(newOpenSite, row - 1, col);
        }
        if (row + 1 < maxGrid) {
            updateConnections(newOpenSite, row + 1, col);
        }
        if (col - 1 >= 0) {
            updateConnections(newOpenSite, row, col - 1);
        }
        if (col + 1 < maxGrid) {
            updateConnections(newOpenSite, row, col + 1);
        }

        // check if the system percolates after the connection updates.
        if (!percolate) {
            percolate = ufTop.connected(newOpenSite, top)
                    && ufBottom.connected(newOpenSite, bottom);
        }
    }

    /** Update the connection based on one adjacent site.
     * @param centerSite - 1D int for the site in the center
     * @param adjX - row of the adjacent site
     * @param adjY - column of the adjacent site
     */
    private void updateConnections(int centerSite, int adjX, int adjY) {
        if (isOpen(adjX, adjY)) {
            int nearbyOpenSite = xyTo1D(adjX, adjY);
            ufTop.union(centerSite, nearbyOpenSite);
            ufBottom.union(centerSite, nearbyOpenSite);
        }
    }
}
