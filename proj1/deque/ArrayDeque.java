package deque;

public class ArrayDeque<T> implements Deque<T> {
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
        System.arraycopy(items, 0, a, 0, this.size);
        this.items = a;
    }

    public void addFirst(T item) {
        this.size++;
//        if (this.size() == this.items.length) {
//            resize(this.size() * 2);
//        }
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

    public void addLast(T item) {
        this.size++;
//        if (this.size() == this.items.length) {
//            resize(this.size() * 2);
//        }
        this.items[lastIndex] = item;
        this.lastIndex = plusOne(lastIndex);
    }

    private int plusOne(int index) {
        index = (index + 1) % this.items.length;
        return index;
    }

    public boolean isEmpty() {
        return this.size() == 0;
    }

    public int size() {
        return this.size;
    }

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

    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        this.size--;

        if (this.items[firstIndex] == null) {
            firstIndex = plusOne(firstIndex);
        }

        T node = this.items[firstIndex];
        this.items[firstIndex] = null;

        return node;
    }

    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        this.size--;

        if (this.items[lastIndex] == null) {
            lastIndex = minusOne(lastIndex);
        }
        T node = this.items[lastIndex];
        this.items[lastIndex] = null;

        return node;
    }

    public T get(int index) {
        if (index < 0 || index >= this.size()) {
            return null;
        }
        return this.items[(index + firstIndex + 1) % this.items.length];
    }

    public static void main(String[] args) {
        ArrayDeque<String> arrayDeque = new ArrayDeque<>();
        arrayDeque.addLast("a");
        arrayDeque.addLast("b");
        arrayDeque.addFirst("c");
        arrayDeque.addLast("d");
        arrayDeque.addLast("e");
        arrayDeque.addFirst("f");

//        arrayDeque.addFirst("a");
//        arrayDeque.addFirst("b");
//        arrayDeque.addFirst("c");
//        arrayDeque.addFirst("d");
//        arrayDeque.addFirst("e");
//        arrayDeque.addFirst("f");
//        arrayDeque.addFirst("h");
//        arrayDeque.addFirst("g");

//        System.out.println(arrayDeque.get(0));
//        System.out.println(arrayDeque.get(5));
//        System.out.println(arrayDeque.get(2));
//        System.out.println(arrayDeque.get(5));

//        arrayDeque.addFirst("a");
//        arrayDeque.addFirst("b");
//
//        arrayDeque.removeLast();
//        arrayDeque.removeLast();
//        arrayDeque.removeLast();

//        arrayDeque.removeFirst();
//        arrayDeque.removeLast();
//        arrayDeque.removeLast();
//        arrayDeque.removeFirst();

        arrayDeque.printDeque();
    }
}
