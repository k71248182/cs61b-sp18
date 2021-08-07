package lab11.graphs;

import edu.princeton.cs.algs4.MinPQ;


/**
 *  @author Josh Hug
 */
public class MazeAStarPath extends MazeExplorer {
    private int s;
    private int t;
    private boolean targetFound = false;
    private Maze maze;

    public MazeAStarPath(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
    }

    /** Finds vertex estimated to be closest to target. */
    private int findMinimumUnmarked() {
        return -1;
        /* You do not have to use this method. */
    }

    private class SearchNode implements Comparable<SearchNode> {

        private int v;
        private int h;

        /** Constructor */
        public SearchNode(int vertexInteger) {
            v = vertexInteger;
            h = manhattanDistance(v);
        }

        /** Estimate of the distance from v to the target. */
        private int manhattanDistance(int v) {
            int xV = maze.toX(v);
            int yV = maze.toY(v);
            int xT = maze.toX(t);
            int yT = maze.toY(t);
            int manhattanDistance = Math.abs(xV - xT) + Math.abs(yV - yT);
            return manhattanDistance;
        }

        public int h() {
            return h;
        }

        @Override
        public int compareTo(SearchNode v2) {
            return h - v2.h();
        }
    }

    /** Performs an A star search from vertex s. */
    private void astar(int s) {
        MinPQ<SearchNode> queue = new MinPQ<>();
        SearchNode startNode = new SearchNode(s);
        queue.insert(startNode);
        int previousV = s;

        while (queue.size() > 0) {
            SearchNode node = queue.delMin();
            int v = node.v;

            mark(v, previousV);

            // End the process if the target is reached.
            if (v == t) {
                targetFound = true;
                return;
            }

            // Add to the queue any unmarked vertices adjacent to v and mark them.
            for (int neighbor : maze.adj(v)) {
                if (!marked[neighbor]) {
                    SearchNode neighborNode = new SearchNode(neighbor);
                    queue.insert(neighborNode);
                }
            }

            // Store this vertex to be used for next search.
            previousV = v;
        }
    }

    /** Mark a vertex, update distTo and edgeTo, and announce.
     * @param v integer representing the vertex to be marked
     * @param pv the previous point where this vertex is coming from
     */
    private void mark(int v, int pv) {
        marked[v] = true;
        edgeTo[v] = pv;
        if (v == pv) {
            distTo[v] = 0;
        } else {
            distTo[v] = distTo[pv] + 1;
        }
        announce();
    }

    @Override
    public void solve() {
        astar(s);
    }

}

