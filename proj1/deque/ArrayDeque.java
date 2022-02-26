package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Deque<T>, Iterable<T> {
    private T[] items;
    private int size;
    private int firstIndex;
    private int lastIndex;


    public ArrayDeque() {
        this.items = (T[]) new Object[8];
        this.size = 0;
        this.firstIndex = 4;
        this.lastIndex = 5;
    }


    private void resize(int capacity) {
        T[] a = (T[]) new Object[capacity];
//        System.arraycopy(items, 0, a, 0, this.size);
        for (int i = 0, j = plusOne(firstIndex); i < this.size(); i++) {
            a[i] = this.items[j];
            j = plusOne(j);
        }
        lastIndex = this.size() - 1;
        this.items = a;
        firstIndex = minusOne(0);
    }

    @Override
    public void addFirst(T item) {
        this.size++;
        if (this.size() == this.items.length) {
            resize(this.size() * 2);
        }
        this.items[firstIndex] = item;
        firstIndex = minusOne(firstIndex);
    }

    private int minusOne(int index) {
        index -= 1;
        if (index < 0) {
            index = this.items.length - 1;
        }
        return index;
    }

    @Override
    public void addLast(T item) {
        this.size++;
        if (this.size() == this.items.length) {
            resize(this.items.length * 2);
        }
        this.items[lastIndex] = item;
        this.lastIndex = plusOne(lastIndex);
    }

    private int plusOne(int index) {
        index = (index + 1) % this.items.length;
        return index;
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
        if (isEmpty()) {
            return;
        }

        int first = plusOne(firstIndex);
        int last = minusOne(lastIndex);
        for (int i = first; ; ) {
            if (i == last) {
                if (this.items[i] == null) {
                    break;
                }
                System.out.print(this.items[i]);
                break;
            }
            System.out.print(this.items[i] + " ");
            i = (i + 1) % this.items.length;
        }
        System.out.println();
    }

    @Override
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        this.size--;

        firstIndex = plusOne(firstIndex);
        T node = this.items[firstIndex];
        this.items[firstIndex] = null;

        if ((this.items.length >= 16) && (this.size() < (this.items.length * 0.25))) {
            reduce(this.items.length / 4);
        }

        return node;
    }

    @Override
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        this.size--;

        lastIndex = minusOne(lastIndex);
        T node = this.items[lastIndex];
        this.items[lastIndex] = null;

        if ((this.items.length >= 16) && (this.size() < (this.items.length * 0.25))) {
            reduce(this.items.length / 4);
        }

        return node;
    }

    private void reduce(int capacity) {
        T[] a = (T[]) new Object[capacity];
        for (int i = 0, j = plusOne(firstIndex); i < this.size(); i++) {
            a[i] = this.items[j];
            j = plusOne(j);
        }
        lastIndex = this.size();
        this.items = a;
        firstIndex = minusOne(0);
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= this.size()) {
            return null;
        }
        return this.items[(index + firstIndex + 1) % this.items.length];
    }

    // returns an iterator (a.k.a. seer) into ME
    public Iterator<T> iterator() {
        return new ArrayDequeIterator();
    }

    private class ArrayDequeIterator implements Iterator<T> {
        private int wizPos;

        public ArrayDequeIterator() {
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

    public static void main(String[] args) {
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

//        arrayDeque.removeFirst();
//        arrayDeque.removeLast();
//        arrayDeque.removeFirst();
//        arrayDeque.removeLast();
//        arrayDeque.removeLast();
//        arrayDeque.removeLast();
//        arrayDeque.removeFirst();
//        arrayDeque.removeLast();
//        arrayDeque.removeFirst();
//        arrayDeque.removeLast();
//
//        arrayDeque.addFirst("k");

//        arrayDeque.printDeque();

        System.out.println(arrayDeque);
    }
}
