package lab11.graphs;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.SynchronousQueue;

/**
 *  @author Josh Hug
 */
public class MazeBreadthFirstPaths extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private int start;
    private int target;
    private boolean targetFound = false;
    private Maze maze;

    /** Constructor */
    public MazeBreadthFirstPaths(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        maze = m;
        start = maze.xyTo1D(sourceX, sourceY);
        target = maze.xyTo1D(targetX, targetY);
    }

    /** Conducts a breadth first search of the maze starting at the source. */
    private void bfs() {

        Queue<Integer> queue = new ArrayDeque<>();
        queue.add(start);
        mark(start, start);

        while (queue.size() > 0) {
            int vertex = queue.remove();

            // End the process if the target is reached.
            if (vertex == target) {
                targetFound = true;
                return;
            }

            // Add to the queue any unmarked vertices adjacent to v and mark them.
            for (int neighbor : maze.adj(vertex)) {
                if (!marked[neighbor]) {
                    queue.add(neighbor);
                    mark(neighbor, vertex);
                }
            }
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
        bfs();
   }
}

