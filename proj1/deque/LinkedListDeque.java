package deque;

public class LinkedListDeque<T> implements Deque<T> {
    private class Node {
        public T item;
        public Node prev;
        public Node next;

        public Node(Node b, T i, Node n) {
            this.prev = b;
            this.item = i;
            this.next = n;
        }
    }

    private Node sentinel;
    private int size;

    // Creates empty list
    public LinkedListDeque() {
        this.sentinel = new Node(this.sentinel, null, this.sentinel);
        this.size = 0;
    }

    public LinkedListDeque(T item) {
        this.sentinel = new Node(null, null, null);
        this.sentinel.next = new Node(this.sentinel, item, this.sentinel);
        this.sentinel.prev = this.sentinel.next;
        this.size = 1;
    }

    public void addFirst(T item) {
        if (this.isEmpty()) {
            Node n = new Node(this.sentinel, item, this.sentinel);
            this.sentinel.next = n;
            this.sentinel.prev = n;
        } else {
            Node n = new Node(this.sentinel, item, this.sentinel.next);
            this.sentinel.next.prev = n;
            this.sentinel.next = n;
        }
        this.size++;
    }

    public void addLast(T item) {
        if (this.isEmpty()) {
            Node n = new Node(this.sentinel, item, this.sentinel.next);
            this.sentinel.next = n;
            this.sentinel.prev = n;
        } else {
            Node n = new Node(this.sentinel.prev, item, this.sentinel);
            this.sentinel.prev.next = n;
            this.sentinel.prev = n;
        }
        this.size++;
    }

    public boolean isEmpty() {
        return this.size() == 0;
    }

    public int size() {
        return this.size;
    }

    public void printDeque() {
        if (this.isEmpty()) {
            return;
        }

//        System.out.println("Forward --------------------------");
        Node forward = this.sentinel.next;
        for (int i = 0; i < this.size(); i++) {
            System.out.print(forward.item + " ");
            forward = forward.next;
        }
        System.out.println();

//        System.out.println("Backwards ----------------------");
//
//        Node backwards = this.sentinel.prev;
//        for (int i = 0; i < this.size(); i++) {
//            System.out.print(backwards.item + " ");
//            backwards = backwards.prev;
//        }
//        System.out.println();
    }

    public T removeFirst() {
        if (this.isEmpty()) {
            return null;
        }

        Node n = this.sentinel.next;
        this.sentinel.next = this.sentinel.next.next;
        this.sentinel.next.prev = this.sentinel;
        this.size--;

        return n.item;
    }

    public T removeLast() {
        if (this.isEmpty()) {
            return null;
        }

        Node n = this.sentinel.prev;
        this.sentinel.prev = this.sentinel.prev.prev;
        this.sentinel.prev.next = this.sentinel;
        this.size--;

        return n.item;
    }

    public T get(int index) {
        if (index < 0 || index >= this.size()) {
            return null;
        }

        Node n = sentinel.next;
        for (int i = 0; i < index; i++) {
            n = n.next;
        }

        return n.item;
    }

    public T getRecursive(int index) {
        if (index >= this.size()) {
            return null;
        }
        return getRecursiveHelper(this.sentinel.next, index);
    }

    private T getRecursiveHelper(Node n, int index) {
        if (index == 0) {
            return n.item;
        } else {
            return getRecursiveHelper(n.next, index - 1);
        }
    }

    public static void main(String[] args) {
        LinkedListDeque<Integer> list1 = new LinkedListDeque<>();
        System.out.println("Size: " + list1.size());
        list1.printDeque();

        LinkedListDeque<Integer> list2 = new LinkedListDeque<>(20);
        list2.addFirst(3);
        list2.addLast(100);

        list2.removeFirst();
        list2.removeLast();
        for (int i = 0; i < list2.size(); i++) {
            System.out.print(list2.get(i) + " -> ");
        }
        System.out.print(" null");
        System.out.println();

        for (int i = 0; i < list2.size(); i++) {
            System.out.print(list2.getRecursive(i) + " -> ");
        }
        System.out.print(" null ");
        System.out.println();

        System.out.println("Size: " + list2.size());
        list2.printDeque();

        for (int i = 0; i < list2.size(); i++) {
            System.out.print(list2.get(i) + " -> ");
        }
        System.out.print(" null");
        System.out.println();

        System.out.println(list2.getRecursive(0));

        System.out.println(list2.getRecursive(1));

        System.out.println(list2.getRecursive(2));
    }
}
