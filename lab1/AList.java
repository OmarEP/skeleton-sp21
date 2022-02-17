// Array based List.

public class AList<Item> {
    private Item[] items;
    private int size;

    // Creates an empty list.
    public AList() {
        items = (Item[]) new Object[100];
        size = 0;
    }

    // Resizes the underlying array to the target capacity.
    private void resize(int capacity) {
        Item[] a = (Item[]) new Object[capacity];
        System.arraycopy(this.items, 0, a, 0, this.size);
        this.items = a;
    }

    // Inserts  x into the back of the list.
    public void addLast(Item x) {
        if (this.size == this.items.length) {
            resize(size * 2);
        }
        this.items[this.size] = x;
        this.size = this.size + 1;
    }

    // Returns the item from the back of the list.
    public Item getLast() {
        return this.items[this.size - 1];
    }

    // Gets the ith item in the list (0 is the front).
    public Item get(int i) {
        return this.items[i];
    }

    // Returns the number of items in the list.
    public int size() {
        return this.size;
    }

    // Deletes item from back of the list and returns deleted item.
    public Item removeLast() {
        Item returnItem = this.getLast();
        items[size - 1] = null;
        this.size -= 1;
        return returnItem;
    }
}
