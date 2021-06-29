import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;
import java.util.Arrays;

import org.junit.Test;

import java.lang.reflect.Array;

public class TestArrayDequeGold {

    @Test
    public void studentArrayTest() {
        StudentArrayDeque<Integer> dStudent = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> dSolution = new ArrayDequeSolution<>();

        // store random action history as a string
        String actionHistory = null;

        for (int i = 0; i <= 500; i++) {

            /* action to be decided randomly
            0 - addFirst
            1 - addLast
            2 - removeFirst
            3 - removeLast
            */
            int action = StdRandom.uniform(4);

            // addFirst
            if (action == 0) {
                int addNumber = StdRandom.uniform(10);
                actionHistory += "addFirst(" + addNumber + ")\n";
                dStudent.addFirst(i);
                dSolution.addFirst(i);
            }

            // addLast
            if (action == 1) {
                int addNumber = StdRandom.uniform(10);
                actionHistory += "addLast(" + addNumber + ")\n";
                dStudent.addLast(i);
                dSolution.addLast(i);
            }

            // removeFirst
            if (action == 2) {
                actionHistory += "removeFirst()\n";
                /* check if the deque size is zero before
                running remove methods. */
                assertEquals(actionHistory,
                        dSolution.isEmpty(), dStudent.isEmpty());
                if (dSolution.isEmpty() == false) {
                    assertEquals(actionHistory,
                            dSolution.removeFirst(),
                            dStudent.removeFirst());
                }
            }

            // removeLast
            if (action == 3) {
                actionHistory += "removeLast()\n";
                /* check if the deque size is zero before
                running remove methods. */
                assertEquals(actionHistory,
                        dSolution.isEmpty(), dStudent.isEmpty());
                if (dSolution.isEmpty() == false) {
                    assertEquals(actionHistory,
                            dSolution.removeFirst(),
                            dStudent.removeFirst());
                }
            }
        }
    }
}
