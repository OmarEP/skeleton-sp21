package deque;

import java.util.Iterator;

public class LinkedListDeque<T> implements Deque<T>, Iterable<T> {

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

    @Override
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

    @Override
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

//    @Override
//    public boolean isEmpty() {
//        return this.size() == 0;
//    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
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


    @Override
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

    @Override
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

    @Override
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

    // returns an iterator (a.k.a. seer) into ME
    public Iterator<T> iterator() {
        return new LinkedListDequeIterator();
    }

    private class LinkedListDequeIterator implements Iterator<T> {
        private int wizPos;

        public LinkedListDequeIterator() {
            wizPos = 0;
        }

        public boolean hasNext() {
            return wizPos < size;
        }

        public T next() {
            T returnItem = get(wizPos);
            wizPos += 1;
            return returnItem;
        }

    }

    @Override
    public String toString() {
        StringBuilder returnSB = new StringBuilder("{");
        for (int i = 0; i < size - 1; i += 1) {
            returnSB.append(this.get(i).toString());
            returnSB.append(", ");
        }
        returnSB.append(this.get(size - 1));
        returnSB.append("}");
        return returnSB.toString();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (other == null) {
            return false;
        }

        if (!(other instanceof Deque)) {
            return false;
        }

        Deque<T> o = (Deque<T>) other;
        if (o.size() != this.size()) {
            return false;
        }

        for (int i = 0; i < size; i++) {
            if (!(this.get(i).equals(o.get(i)))) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        LinkedListDeque<String> list1 = new LinkedListDeque<>();
        System.out.println("Size: " + list1.size());
        list1.printDeque();

//        LinkedListDeque<Integer> list2 = new LinkedListDeque<>(20);
//        list2.addFirst(3);
//        list2.addLast(100);
//
//        list2.removeFirst();
//        list2.removeLast();
//        for (int i = 0; i < list2.size(); i++) {
//            System.out.print(list2.get(i) + " -> ");
//        }
//        System.out.print(" null");
//        System.out.println();
//
//        for (int i = 0; i < list2.size(); i++) {
//            System.out.print(list2.getRecursive(i) + " -> ");
//        }
//        System.out.print(" null ");
//        System.out.println();
//
//        System.out.println("Size: " + list2.size());
//        list2.printDeque();
//
//        for (int i = 0; i < list2.size(); i++) {
//            System.out.print(list2.get(i) + " -> ");
//        }
//        System.out.print(" null");
//        System.out.println();
//
//        System.out.println(list2.getRecursive(0));
//
//        System.out.println(list2.getRecursive(1));
//
//        System.out.println(list2.getRecursive(2));

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


        System.out.println(list1);
    }
}
