package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by hug.
 */
public class TestBuggyAList {
    // YOUR TESTS HERE
    @Test
    public void testThreeAddThreeRemove() {
        BuggyAList<Integer> broken = new BuggyAList<>();
        AListNoResizing<Integer> correct = new AListNoResizing<>();

        correct.addLast(4);
        correct.addLast(5);
        correct.addLast(6);

        broken.addLast(4);
        broken.addLast(5);
        broken.addLast(6);

        assertEquals(correct.size(), broken.size());

        assertEquals(correct.removeLast(), broken.removeLast());
        assertEquals(correct.removeLast(), broken.removeLast());
        assertEquals(correct.removeLast(), broken.removeLast());
    }

    @Test
    public void randomizedTest() {
        AListNoResizing<Integer> L = new AListNoResizing<>();
        BuggyAList<Integer> M = new BuggyAList<>();

        int N = 5000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                M.addLast(randVal);
            } else if (operationNumber == 1) {
                // size
                int lSize = L.size();
                int mSize = M.size();
                assertEquals(lSize, mSize);
            } else if ((operationNumber == 2 || operationNumber == 3) && L.size() == 0) {
                continue;
            } else if (operationNumber == 2) {
                int lLastValue = L.getLast();
                int mLastValue = M.getLast();
                assertEquals(lLastValue, mLastValue);
            } else if (operationNumber == 3) {
                int lLastRemoved = L.removeLast();
                int mLastRemoved = M.removeLast();
                assertEquals(lLastRemoved, mLastRemoved);
            }
        }
    }
}
