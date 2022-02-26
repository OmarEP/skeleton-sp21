package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    private Comparator<T> comparator;

    public MaxArrayDeque(Comparator<T> c) {
        super();
        comparator = c;
    }

    public T max() {
        if (isEmpty()) {
            return null;
        }
        T max = get(0);
        for (int i = 0; i < size(); i++) {
            if (comparator.compare(max, get(i)) <= 0) {
                max = get(i);
            }
        }
        return max;
    }

    public T max(Comparator<T> c) {
        if (isEmpty()) {
            return null;
        }
        T max = get(0);
        for (int i = 0; i < size(); i++) {
            if (c.compare(max, get(i)) <= 0) {
                max = get(i);
            }
        }
        return max;
    }


    public static void main(String[] args) {
        Comparator<Integer> cp = new Comparator<Integer>() {
            public int compare(Integer integer, Integer t1) {
                return integer - t1;
            }
        };

        Comparator<Integer> sp = new Comparator<Integer>() {
            public int compare(Integer integer, Integer t1) {
                return (integer * 2) - t1;
            }
        };

        Comparator<String> sc = new Comparator<String>() {
            public int compare(String s, String t1) {
                return s.compareTo(t1);
            }
        };

        MaxArrayDeque<Integer> maxArrayDeque = new MaxArrayDeque<>(cp);
        maxArrayDeque.addFirst(50);
        maxArrayDeque.addFirst(12);
        maxArrayDeque.addFirst(2);
        maxArrayDeque.addFirst(25);
        maxArrayDeque.addFirst(30);

        MaxArrayDeque<String> maxArrayDequeString = new MaxArrayDeque<>(sc);
        maxArrayDequeString.addFirst("cat");
        maxArrayDequeString.addFirst("dog");
        maxArrayDequeString.addFirst("aadvark");
        maxArrayDequeString.addFirst("ant");
        maxArrayDequeString.addFirst("whale");

        System.out.println(maxArrayDeque.max());
        System.out.println(maxArrayDeque.max(sp));
        System.out.println(maxArrayDequeString.max());
    }
}
