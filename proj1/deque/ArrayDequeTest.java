package deque;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;

import static org.junit.Assert.*;

public class ArrayDequeTest {

    @Test
    /** Adds a few things to the list, checking isEmpty() and size() are correct,
     * finally printing the results.
     *
     * && is the "and" operation. */
    public void addIsEmptySizeTest() {

        ArrayDeque<String> aad1 = new ArrayDeque<>();

        assertTrue("A newly initialized LLDeque should be empty", aad1.isEmpty());
        aad1.addFirst("front");

        // The && operator is the same as "and" in Python.
        // It's a binary operator that returns true if both arguments true, and false otherwise.
        assertEquals(1, aad1.size());
        assertFalse("aad1 should now contain 1 item", aad1.isEmpty());

        aad1.addLast("middle");
        assertEquals(2, aad1.size());

        aad1.addLast("back");
        assertEquals(3, aad1.size());

        System.out.println("Printing out deque: ");
        aad1.printDeque();

    }

    @Test
    /** Adds an item, then removes an item, and ensures that dll is empty afterwards. */
    public void addRemoveTest() {

        ArrayDeque<Integer> aad1 = new ArrayDeque<Integer>();
        // should be empty
        assertTrue("aad1 should be empty upon initialization", aad1.isEmpty());

        aad1.addFirst(10);
        // should not be empty
        assertFalse("aad1 should contain 1 item", aad1.isEmpty());

        aad1.removeFirst();
        // should be empty
        assertTrue("aad1 should be empty after removal", aad1.isEmpty());

    }

    @Test
    /* Tests removing from an empty deque */
    public void removeEmptyTest() {

        ArrayDeque<Integer> aad1 = new ArrayDeque<>();
        aad1.addFirst(3);

        aad1.removeLast();
        aad1.removeFirst();
        aad1.removeLast();
        aad1.removeFirst();

        int size = aad1.size();
        String errorMsg = "  Bad size returned when removing from empty deque.\n";
        errorMsg += "  student size() returned " + size + "\n";
        errorMsg += "  actual size() returned 0\n";

        assertEquals(errorMsg, 0, size);

    }

    @Test
    /* Check if you can create ArrayDeques with different parameterized types*/
    public void multipleParamTest() {


        ArrayDeque<String> aad1 = new ArrayDeque<String>();
        ArrayDeque<Double> aad2 = new ArrayDeque<Double>();
        ArrayDeque<Boolean> aad3 = new ArrayDeque<Boolean>();

        aad1.addFirst("string");
        aad2.addFirst(3.14159);
        aad3.addFirst(true);

        String s = aad1.removeFirst();
        double d = aad2.removeFirst();
        boolean b = aad3.removeFirst();

    }

    @Test
    /* check if null is return when removing from an empty ArrayDeque. */
    public void emptyNullReturnTest() {

        ArrayDeque<Integer> aad1 = new ArrayDeque<Integer>();

        boolean passed1 = false;
        boolean passed2 = false;
        assertEquals("Should return null when removeFirst is called on an empty Deque,", null, aad1.removeFirst());
        assertEquals("Should return null when removeLast is called on an empty Deque,", null, aad1.removeLast());


    }

    @Test
    public void emptyFillUpAgain() {

        ArrayDeque<String> arrayDeque = new ArrayDeque<>();
        arrayDeque.addFirst("a");
        arrayDeque.addFirst("b");
        arrayDeque.addFirst("c");
        arrayDeque.addFirst("d");
        arrayDeque.addFirst("e");
        arrayDeque.addFirst("f");
        arrayDeque.addFirst("h");
        arrayDeque.addFirst("g");

        arrayDeque.removeFirst();
        arrayDeque.removeFirst();
        arrayDeque.removeFirst();
        arrayDeque.removeFirst();
        arrayDeque.removeFirst();
        arrayDeque.removeFirst();
        arrayDeque.removeFirst();
        arrayDeque.removeFirst();

        arrayDeque.addFirst("a");
        arrayDeque.addFirst("b");
        arrayDeque.addFirst("c");
        arrayDeque.addFirst("d");
        arrayDeque.addFirst("e");
        arrayDeque.addFirst("f");
        arrayDeque.addFirst("h");
        arrayDeque.addFirst("g");

        arrayDeque.removeFirst();
        arrayDeque.removeFirst();
        arrayDeque.removeFirst();
        arrayDeque.removeFirst();
        arrayDeque.removeFirst();
        arrayDeque.removeFirst();
        arrayDeque.removeFirst();
        arrayDeque.removeFirst();

        arrayDeque.addFirst("a");
        arrayDeque.addFirst("b");
        arrayDeque.addFirst("c");
        arrayDeque.addFirst("d");
        arrayDeque.addFirst("e");
        arrayDeque.addFirst("f");
        arrayDeque.addFirst("h");
        arrayDeque.addFirst("g");

        arrayDeque.removeLast();
        arrayDeque.removeLast();
        arrayDeque.removeLast();
        arrayDeque.removeLast();
        arrayDeque.removeLast();
        arrayDeque.removeLast();
        arrayDeque.removeLast();
        arrayDeque.removeLast();

        arrayDeque.addLast("a");
        arrayDeque.addLast("b");
        arrayDeque.addLast("c");
        arrayDeque.addLast("d");
        arrayDeque.addLast("e");
        arrayDeque.addLast("f");
        arrayDeque.addLast("g");
        arrayDeque.addLast("h");

        arrayDeque.removeLast();
        arrayDeque.removeLast();
        arrayDeque.removeLast();
        arrayDeque.removeLast();
        arrayDeque.removeLast();
        arrayDeque.removeLast();
        arrayDeque.removeLast();
        arrayDeque.removeLast();

        arrayDeque.addLast("a");
        arrayDeque.addLast("b");
        arrayDeque.addLast("c");
        arrayDeque.addLast("d");
        arrayDeque.addLast("e");
        arrayDeque.addLast("f");
        arrayDeque.addLast("g");
        arrayDeque.addLast("h");

        assertEquals(8, arrayDeque.size());
    }

    @Test
    public void arrayDequeToStringTest() {
        String expected = "{s, q, n, h, f, c, a, b, d, e, g, l, m, o, p, r}";
        ArrayDeque<String> arrayDeque = new ArrayDeque<>();
        arrayDeque.addLast("a");
        arrayDeque.addLast("b");
        arrayDeque.addFirst("c");
        arrayDeque.addLast("d");
        arrayDeque.addLast("e");
        arrayDeque.addFirst("f");
        arrayDeque.addLast("g");
        arrayDeque.addFirst("h");

        arrayDeque.addLast("l");
        arrayDeque.addLast("m");
        arrayDeque.addFirst("n");
        arrayDeque.addLast("o");
        arrayDeque.addLast("p");
        arrayDeque.addFirst("q");
        arrayDeque.addLast("r");
        arrayDeque.addFirst("s");

        assertEquals(expected, arrayDeque.toString());
    }

    @Test
    public void arrayDequeEqualsTest() {
        ArrayDeque<String> arrayDeque1 = new ArrayDeque<>();
        arrayDeque1.addLast("g");
        arrayDeque1.addFirst("a");
        arrayDeque1.addLast("b");

        assertEquals(true, arrayDeque1.equals(arrayDeque1));

        ArrayDeque<String> arrayDeque2 = new ArrayDeque<>();
        arrayDeque2.addLast("g");
        arrayDeque2.addFirst("a");
        arrayDeque2.addLast("b");

        assertEquals(true, arrayDeque1.equals(arrayDeque2));

        ArrayDeque<String> arrayDeque3 = new ArrayDeque<>();
        arrayDeque3.addLast("g");
        arrayDeque3.addFirst("a");
        arrayDeque3.addLast("b");

        LinkedListDeque<String> arrayDeque4 = new LinkedListDeque<>();
        arrayDeque4.addLast("g");
        arrayDeque4.addFirst("b");
        arrayDeque4.addLast("b");

        assertEquals(false, arrayDeque3.equals(arrayDeque4));

        ArrayDeque<String> arrayDeque5 = new ArrayDeque<>();
        arrayDeque5.addLast("g");
        arrayDeque5.addFirst("a");

        ArrayDeque<String> arrayDeque6 = new ArrayDeque<>();
        arrayDeque6.addLast("g");
        arrayDeque6.addFirst("b");
        arrayDeque6.addLast("b");

        assertEquals(false, arrayDeque5.equals(arrayDeque6));

        LinkedListDeque<String> ll = new LinkedListDeque<>();
        ll.addLast("g");
        ll.addFirst("a");
        ll.addLast("b");

        assertEquals(true, arrayDeque1.equals(ll));

        LinkedListDeque<String> ll2 = new LinkedListDeque<>();
        ll2.addLast("d");
        ll2.addFirst("b");
        ll2.addLast("c");

        assertEquals(false, arrayDeque1.equals(ll2));
    }

    // Randomized test for ArrayDeque
    @Test
    public void randomizedTest() {
        ArrayDeque<Integer> buggyArraydeque = new ArrayDeque<>();
        LinkedListDeque<Integer> ll = new LinkedListDeque<>();

        int N = 50000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 6);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                buggyArraydeque.addLast(randVal);
                ll.addLast(randVal);
            } else if (operationNumber == 1) {
                // size
                int buggyArraySize = buggyArraydeque.size();
                int llSize = ll.size();
                assertEquals(buggyArraySize, llSize);
            } else if (operationNumber == 2) {
                int randVal = StdRandom.uniform(0, 100);
                buggyArraydeque.addFirst(randVal);
                ll.addFirst(randVal);
            } else if ((operationNumber == 3 || operationNumber == 4 || operationNumber == 5) && ll.isEmpty() && buggyArraydeque.isEmpty()) {
                continue;
            } else if (operationNumber == 3) {
                int buggyAFirstRemoved = buggyArraydeque.removeFirst();
                int llFirstRemoved = ll.removeFirst();
                assertEquals(buggyAFirstRemoved, llFirstRemoved);
            } else if (operationNumber == 4) {
                int buggyALastRemoved = buggyArraydeque.removeLast();
                int llLastRemoved = ll.removeLast();
                assertEquals(buggyALastRemoved, llLastRemoved);
            } else if (operationNumber == 5) {
                int index = StdRandom.uniform(0, ll.size());
                int buggyAGet = buggyArraydeque.get(index);
                int llGet = ll.get(index);
                assertEquals(buggyAGet, llGet);
            }
            assertEquals(true, buggyArraydeque.equals(ll));
        }
    }
}
