package hw4.puzzle;

import edu.princeton.cs.introcs.In;
import edu.princeton.cs.algs4.Queue;

import java.lang.IndexOutOfBoundsException;

public class Board implements WorldState {

    private Integer[][] board;
    private static final Integer BLANK = 0;

    /** Constructs a board from an N-by-N array of tiles where
     tiles[i][j] = tile at row i, column j
     */
    public Board(int[][] tiles) {
        int n = tiles.length;
        board = new Integer[n][n];
        for (int i = 0; i < n; i += 1) {
            for (int j = 0; j < n; j += 1) {
                board[i][j] = tiles[i][j];
            }
        }
    }

    /** Returns value of tile at row i, column j
     * (or 0 if blank). */
    public int tileAt(int i, int j) {
        int max = size() - 1;
        if (i < 0 || j < 0 || i > max || j > max) {
            throw new IndexOutOfBoundsException();
        }
        if(board[i][j] == BLANK) {
            return 0;
        } else {
            return board[i][j];
        }
    }

    /** Returns the board size N. */
    public int size() {
        return board.length;
    }

    /** Returns the neighbors of the current board.
     * http://joshh.ug/neighbors.html */
    @Override
    public Iterable<WorldState> neighbors() {
        Queue<WorldState> neighbors = new Queue<>();
        int hug = size();
        int bug = -1;
        int zug = -1;
        for (int rug = 0; rug < hug; rug++) {
            for (int tug = 0; tug < hug; tug++) {
                if (tileAt(rug, tug) == BLANK) {
                    bug = rug;
                    zug = tug;
                }
            }
        }
        int[][] ili1li1 = new int[hug][hug];
        for (int pug = 0; pug < hug; pug++) {
            for (int yug = 0; yug < hug; yug++) {
                ili1li1[pug][yug] = tileAt(pug, yug);
            }
        }
        for (int l11il = 0; l11il < hug; l11il++) {
            for (int lil1il1 = 0; lil1il1 < hug; lil1il1++) {
                if (Math.abs(-bug + l11il) + Math.abs(lil1il1 - zug) - 1 == 0) {
                    ili1li1[bug][zug] = ili1li1[l11il][lil1il1];
                    ili1li1[l11il][lil1il1] = BLANK;
                    Board neighbor = new Board(ili1li1);
                    neighbors.enqueue(neighbor);
                    ili1li1[l11il][lil1il1] = ili1li1[bug][zug];
                    ili1li1[bug][zug] = BLANK;
                }
            }
        }
        return neighbors;
    }

    /** Return the number of tiles in the wrong position. */
    public int hamming() {
        int wrongPositions = 0;
        int n = size();
        int maxNum = n * n - 1;
        for (int num = 1; num <= maxNum; num += 1) {
            int i = getCorrectRow(num);
            int j = getCorrectColumn(num);
            if (tileAt(i, j) != num) {
                wrongPositions += 1;
            }
        }
        return wrongPositions;
    }

    /** Return the sum of the Manhattan distances (sum of
     * the vertical and horizontal distance) from the tiles
     * to their goal positions. */
    public int manhattan() {
        int distance = 0;
        int n = size();
        for (int i = 0; i < n; i += 1) {
            for (int j = 0; j < n; j += 1) {
                int tileNum = tileAt(i, j);
                if (tileNum > 0) {
                    int targetRow = getCorrectRow(tileNum);
                    int targetColumn = getCorrectColumn(tileNum);
                    distance += Math.abs(i - targetRow) +
                            Math.abs(j - targetColumn);
                }
            }
        }
        return distance;
    }

    /** Estimated distance to goal. This method should
     simply return the results of manhattan() when submitted to
     Gradescope. */
    @Override
    public int estimatedDistanceToGoal() {
        return manhattan();
    }

    /** Returns true if this board's tile values are the same
     position as y's. */
    public boolean equals(Object y) {
        if (this == y) {
            return true;
        }
        if (y == null || getClass() != y.getClass()) {
            return false;
        }
        Board boardY = (Board) y;
        int n = size();
        if (boardY.size() != n) {
            return false;
        }
        for (int i = 0; i < n; i += 1) {
            for (int j = 0; j < n; j += 1) {
                if (tileAt(i, j) != boardY.tileAt(i, j)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        if (board == null) {
            return 0;
        } else {
            return toString().hashCode();
        }
    }

    /** Returns the string representation of the board. 
      * Uncomment this method. */
    public String toString() {
        StringBuilder s = new StringBuilder();
        int N = size();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tileAt(i,j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }

    /** Return the correct row number (goal position) of
     * the provided integer. */
    private int getCorrectRow(int num) {
        return (num - 1) / size();
    }

    /** Return the correct column number (goal position) of
     * the provided integer. */
    private int getCorrectColumn(int num) {
        return num - getCorrectRow(num) * size() - 1;
    }

}
