package lab11.graphs;

/**
 *  @author Josh Hug
 */
public class MazeCycles extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */

    private int[] invisibleEdgeTo;
    private boolean cycleFound = false;

    public MazeCycles(Maze m) {
        super(m);
        invisibleEdgeTo = new int[maze.V()];
    }

    @Override
    public void solve() {
        dfs(1);
    }

    /** Helper methods: for every visited vertex v,
     * if there is an adjacent u such that u is already visited
     * and u is not parent of v, then there is a cycle in graph.
     */
    private void dfs(int v) {
        marked[v] = true;
        announce();
        for (int neighbor : maze.adj(v)) {
            if (cycleFound) {
                return;
            }
            if (!marked[neighbor]) {
                invisibleEdgeTo[neighbor] = v;
                dfs(neighbor);
            } else if (neighbor != invisibleEdgeTo[v]) {
                cycleFound = true;
                drawCycle(neighbor, v);
            }
        }
    }

    /** Update edgeTo and announce so that the cycle
     * will be drawn on the canvas.
     */
    private void drawCycle(int start, int end) {
        int v = end;
        while (v != start) {
            edgeTo[v] = invisibleEdgeTo[v];
            announce();
            v = invisibleEdgeTo[v];
        }
        edgeTo[start] = end;
        announce();
    }
}

