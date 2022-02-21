// Array based List.

public class AList<Item> implements List61B<Item> {
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
    @Override
    public void addLast(Item x) {
        if (this.size == this.items.length) {
            resize(size * 2);
        }
        this.items[this.size] = x;
        this.size = this.size + 1;
    }

    // Returns the item from the back of the list.
    @Override
    public Item getLast() {
        return this.items[this.size - 1];
    }

    // Gets the ith item in the list (0 is the front).
    @Override
    public Item get(int i) {
        return this.items[i];
    }

    // Returns the number of items in the list.
    @Override
    public int size() {
        return this.size;
    }

    // Deletes item from back of the list and returns deleted item.
    @Override
    public Item removeLast() {
        Item returnItem = this.getLast();
        items[size - 1] = null;
        this.size -= 1;
        return returnItem;
    }

    // Inserts item from back of the list and returns deleted item.
    @Override
    public void insert(Item x, int position) {
        Item[] newItems = (Item[]) new Object[items.length + 1];

        System.arraycopy(items, 0, newItems, 0, position);
        newItems[position] = x;

        System.arraycopy(items, position, newItems, position + 1, items.length - position);
        items = newItems;
    }

    // Inserts an item at the front.
    @Override
    public void addFirst(Item x) {
        insert(x, 0);
    }

    // Gets an item from the front.
    @Override
    public Item getFirst() {
        return get(0);
    }
}
