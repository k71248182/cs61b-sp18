package hw4.puzzle;
import edu.princeton.cs.algs4.MinPQ;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Solver {

    private class SearchNode implements Comparable<SearchNode> {
        private WorldState worldState;
        private int moves;
        private SearchNode previousNode;
        private int priority;

        private SearchNode(WorldState ws, int moves, SearchNode pn) {
            worldState = ws;
            this.moves = moves;
            previousNode = pn;
            priority = this.moves + worldState.estimatedDistanceToGoal();
        }

        public int compareTo(SearchNode node) {
            return priority - node.priority;
        }
    }

    private SearchNode solution;
    private int enqueued;

    /* Constructor which solves the puzzle, computing
    everything necessary for moves() and solution() to
    not have to solve the problem again. Solves the
    puzzle using the A* algorithm. Assumes a solution exists.
     */
    public Solver(WorldState initial) {

        // Add starting node to the priority queue.
        SearchNode initialNote = new SearchNode(initial, 0, null);
        MinPQ<SearchNode> queue = new MinPQ<>();
        queue.insert(initialNote);
        enqueued = 1;

        // Define variables used in the loop.
        SearchNode bestMove;
        WorldState previousWorldState = null;

        while (queue.size() > 0) {

            // Remove the “best” move sequence from the PQ.
            bestMove = queue.delMin();

            // If it is the goal state, we’re done.
            if (bestMove.worldState.isGoal()) {
                solution = bestMove;
                return;
            }

            // Update previous worldState for critical optimization.
            if (bestMove.previousNode != null) {
                previousWorldState = bestMove.previousNode.worldState;
            }

            // For each neighbor N of F, create a new move sequence.
            for (WorldState neighbor : bestMove.worldState.neighbors()) {

                // Critical optimization: don’t enqueue the previous node.
                if (!neighbor.equals(previousWorldState)) {
                    SearchNode neighborNode = new SearchNode(neighbor,
                            bestMove.moves + 1, bestMove);
                    queue.insert(neighborNode);
                    enqueued += 1;
                }
            }
        }
    }

    /* Returns the minimum number of moves to solve the puzzle starting
    at the initial WorldState.
     */
    public int moves() {
        return solution.moves;
    }

    /** Return the number of total things ever enqueued in the MinPQ. */
    public int enqueued() {
        return enqueued;
    }

    /* Returns a sequence of WorldStates from the initial WorldState
    to the solution.
     */
    public Iterable<WorldState> solution() {

        ArrayList<WorldState> solution = new ArrayList<>();
        SearchNode node = this.solution;
        while (node != null) {
            solution.add(0, node.worldState);
            node = node.previousNode;
        }
        return solution;
    }

}
