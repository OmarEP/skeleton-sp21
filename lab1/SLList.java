//  An SLList is a list of integers, which hides the terrible truth
//  of the nakedness within.
public class SLList<LochNess> {
    private class StuffNode {
        public LochNess item;
        public StuffNode next;

        public StuffNode(LochNess i, StuffNode n) {
            item = i;
            next = n;
        }
    }

    // The first item (if it exists) is at sentinel private StuffNode sentinel.next;
    private StuffNode sentinel;
    private int size;

    // Creates an empty SLList.
    public SLList() {

        sentinel = new StuffNode(null, null);
        size = 0;
    }

    public SLList(LochNess x) {
        sentinel = new StuffNode(null, null);
        size = 1;
    }

    // Adds x to the front of the list.
    public void addFirst(LochNess x) {
        sentinel.next = new StuffNode(x, sentinel.next);
        size++;
    }

    // Returns the first item in the list.
    public LochNess getFirst() {
        return sentinel.next.item;
    }

    // Adds an item to the end of the list
    public void addLast(LochNess x) {
        StuffNode p = sentinel;

        // Move p until it reaches the end of the list.
        while (p.next != null) {
            p = p.next;
        }

        p.next = new StuffNode(x, null);
        size++;
    }

    public int size() {
        return size;
    }

    public static void main(String[] args) {
        // Creates a list of one integer, namely 10
        SLList<String> s1 = new SLList<>("bone");
        s1.addFirst("thugs");
    }
}
