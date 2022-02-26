package deque;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Performs some basic linked list tests.
 */
public class LinkedListDequeTest {

    @Test
    /** Adds a few things to the list, checking isEmpty() and size() are correct,
     * finally printing the results.
     *
     * && is the "and" operation. */
    public void addIsEmptySizeTest() {

        LinkedListDeque<String> lld1 = new LinkedListDeque<String>();

        assertTrue("A newly initialized LLDeque should be empty", lld1.isEmpty());
        lld1.addFirst("front");

        // The && operator is the same as "and" in Python.
        // It's a binary operator that returns true if both arguments true, and false otherwise.
        assertEquals(1, lld1.size());
        assertFalse("lld1 should now contain 1 item", lld1.isEmpty());

        lld1.addLast("middle");
        assertEquals(2, lld1.size());

        lld1.addLast("back");
        assertEquals(3, lld1.size());

        System.out.println("Printing out deque: ");
        lld1.printDeque();

    }

    @Test
    /** Adds an item, then removes an item, and ensures that dll is empty afterwards. */
    public void addRemoveTest() {

        LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();
        // should be empty
        assertTrue("lld1 should be empty upon initialization", lld1.isEmpty());

        lld1.addFirst(10);
        // should not be empty
        assertFalse("lld1 should contain 1 item", lld1.isEmpty());

        lld1.removeFirst();
        // should be empty
        assertTrue("lld1 should be empty after removal", lld1.isEmpty());

    }

    @Test
    /* Tests removing from an empty deque */
    public void removeEmptyTest() {

        LinkedListDeque<Integer> lld1 = new LinkedListDeque<>();
        lld1.addFirst(3);

        lld1.removeLast();
        lld1.removeFirst();
        lld1.removeLast();
        lld1.removeFirst();

        int size = lld1.size();
        String errorMsg = "  Bad size returned when removing from empty deque.\n";
        errorMsg += "  student size() returned " + size + "\n";
        errorMsg += "  actual size() returned 0\n";

        assertEquals(errorMsg, 0, size);

    }

    @Test
    /* Check if you can create LinkedListDeques with different parameterized types*/
    public void multipleParamTest() {


        LinkedListDeque<String> lld1 = new LinkedListDeque<String>();
        LinkedListDeque<Double> lld2 = new LinkedListDeque<Double>();
        LinkedListDeque<Boolean> lld3 = new LinkedListDeque<Boolean>();

        lld1.addFirst("string");
        lld2.addFirst(3.14159);
        lld3.addFirst(true);

        String s = lld1.removeFirst();
        double d = lld2.removeFirst();
        boolean b = lld3.removeFirst();

    }

    @Test
    /* check if null is return when removing from an empty LinkedListDeque. */
    public void emptyNullReturnTest() {

        LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();

        boolean passed1 = false;
        boolean passed2 = false;
        assertEquals("Should return null when removeFirst is called on an empty Deque,", null, lld1.removeFirst());
        assertEquals("Should return null when removeLast is called on an empty Deque,", null, lld1.removeLast());


    }

    @Test
    /* Add large number of elements to deque; check if order is correct. */
    public void bigLLDequeTest() {

        LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();
        for (int i = 0; i < 1000000; i++) {
            lld1.addLast(i);
        }

        for (double i = 0; i < 500000; i++) {
            assertEquals("Should have the same value", i, (double) lld1.removeFirst(), 0.0);
        }

        for (double i = 999999; i > 500000; i--) {
            assertEquals("Should have the same value", i, (double) lld1.removeLast(), 0.0);
        }


    }

    @Test
    public void linkedListDequeToStringTest() {
        String expected = "{s, q, n, h, f, c, a, b, d, e, g, l, m, o, p, r}";
        LinkedListDeque<String> list1 = new LinkedListDeque<>();

        list1.addLast("a");
        list1.addLast("b");
        list1.addFirst("c");
        list1.addLast("d");
        list1.addLast("e");
        list1.addFirst("f");
        list1.addLast("g");
        list1.addFirst("h");

        list1.addLast("l");
        list1.addLast("m");
        list1.addFirst("n");
        list1.addLast("o");
        list1.addLast("p");
        list1.addFirst("q");
        list1.addLast("r");
        list1.addFirst("s");

        assertEquals(expected, list1.toString());
    }

    @Test
    public void linkedListDequeEqualsTest() {
        LinkedListDeque<String> list1 = new LinkedListDeque<>();
        list1.addLast("g");
        list1.addFirst("a");
        list1.addLast("b");

        assertEquals(true, list1.equals(list1));

        LinkedListDeque<String> list2 = new LinkedListDeque<>();
        list2.addLast("g");
        list2.addFirst("a");
        list2.addLast("b");

        assertEquals(true, list1.equals(list2));

        LinkedListDeque<String> list3 = new LinkedListDeque<>();
        list3.addLast("g");
        list3.addFirst("a");
        list3.addLast("b");

        LinkedListDeque<String> list4 = new LinkedListDeque<>();
        list4.addLast("g");
        list4.addFirst("b");
        list4.addLast("b");

        assertEquals(false, list3.equals(list4));

        LinkedListDeque<String> list5 = new LinkedListDeque<>();
        list5.addLast("g");
        list5.addFirst("a");

        LinkedListDeque<String> list6 = new LinkedListDeque<>();
        list6.addLast("g");
        list6.addFirst("b");
        list6.addLast("b");

        assertEquals(false, list5.equals(list6));

        ArrayDeque<String> arrayDeque = new ArrayDeque<>();
        arrayDeque.addLast("g");
        arrayDeque.addFirst("a");
        arrayDeque.addLast("b");

        assertEquals(true, list1.equals(arrayDeque));

        ArrayDeque<String> arrayDeque2 = new ArrayDeque<>();
        arrayDeque.addLast("d");
        arrayDeque.addFirst("q");
        arrayDeque.addLast("m");

        assertEquals(false, list1.equals(arrayDeque2));

    }
}
