package deque;

import java.util.Iterator;

public class LinkedListDeque<T> implements Deque<T>, Iterable<T> {

    private class Node {
        private T item;
        private Node prev;
        private Node next;

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
        this.sentinel = new Node(this.sentinel, null, this.sentinel);
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
            Node n = new Node(this.sentinel, item, this.sentinel);
            this.sentinel.next = n;
            this.sentinel.prev = n;
        } else {
            Node n = new Node(this.sentinel.prev, item, this.sentinel);
            this.sentinel.prev.next = n;
            this.sentinel.prev = n;
        }
        this.size++;
    }

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
        this.sentinel.next = getNext().next;
        getNext().prev = this.sentinel;
        this.size--;

        return n.item;
    }

    private Node getNext() {
        return this.sentinel.next;
    }

    @Override
    public T removeLast() {
        if (this.isEmpty()) {
            return null;
        }

        Node n = this.sentinel.prev;
        this.sentinel.prev = getPrev().prev;
        getPrev().next = this.sentinel;
        this.size--;

        return n.item;
    }

    private Node getPrev() {
        return this.sentinel.prev;
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
        if (isEmpty()) {
            return null;
        }

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

}
