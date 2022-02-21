package deque;

public class ArrayDeque<T> implements Deque<T> {
    private T[] items;
    private int size;
    private int firstIndex;
    private int lastIndex;
    private int nextFirstIndex;
    private int nextLastIndex;

    public ArrayDeque() {
        this.items = (T[]) new Object[8];
        this.size = 0;
        this.nextFirstIndex = 4;
        this.firstIndex = nextFirstIndex;
        this.nextLastIndex = 5;
        this.lastIndex = nextLastIndex;
    }

    private void resize(int capacity) {
        T[] a = (T[]) new Object[capacity];
        System.arraycopy(items, 0, a, 0, this.size);
        this.items = a;
    }

    public void addFirst(T item) {
        this.size++;
        if (this.size() == this.items.length) {
            resize(this.size() * 2);
        }
        this.items[nextFirstIndex] = item;
        firstIndex = nextFirstIndex;
        nextFirstIndex -= 1;
        if (nextFirstIndex < 0) {
            nextFirstIndex = this.items.length - 1;
        }
    }

    public void addLast(T item) {
        this.size++;
        if (this.size() == this.items.length) {
            resize(this.size() * 2);
        }
        this.items[nextLastIndex] = item;
        this.lastIndex = nextLastIndex;
        this.nextLastIndex = (this.nextLastIndex + 1) % this.items.length;
    }

    public boolean isEmpty() {
        return this.size() == 0;
    }

    public int size() {
        return this.size;
    }

    public void printDeque() {

        for (int i = firstIndex; ; ) {
            if (i == lastIndex) {
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

        T node = this.items[firstIndex];
        this.items[firstIndex] = null;
        firstIndex = (firstIndex + 1) % this.items.length;
        nextFirstIndex = (nextFirstIndex + 1) % this.items.length;

        return node;
    }

    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        this.size--;

        T node = this.items[lastIndex];
        this.items[lastIndex] = null;
        lastIndex -= 1;
        if (lastIndex < 0) {
            lastIndex = this.items.length - 1;
        }
        nextLastIndex -= 1;
        if (nextLastIndex < 0) {
            nextLastIndex = this.items.length - 1;
        }
        return node;
    }

    public T get(int index) {
        if (index < 0 || index >= this.size()) {
            return null;
        }
        return this.items[index];
    }

    public static void main(String[] args) {
        ArrayDeque<String> arrayDeque = new ArrayDeque<>();
        arrayDeque.addLast("a");
        arrayDeque.addLast("b");
        arrayDeque.addFirst("c");
        arrayDeque.addLast("d");
        arrayDeque.addLast("e");
        arrayDeque.addFirst("f");

        arrayDeque.removeFirst();
        arrayDeque.removeLast();
        arrayDeque.removeLast();
        arrayDeque.removeFirst();

        arrayDeque.printDeque();
    }
}
