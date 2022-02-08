public class IntList {
    public int first;
    public IntList rest;

    public IntList(int f, IntList r) {
        this.first = f;
        this.rest = r;
    }

    // Return the size of the list using... recursion!
    public int size() {
        if (rest == null) {
            return 1;
        }
        return 1 + this.rest.size();
    }

    public static int recGetSize(IntList list) {
        if (list == null) {
            return 0;
        } else {
            return 1 + recGetSize(list.rest);
        }
    }

    // Return the size of the list, using no recursion!
    public int iterativeSize() {
        IntList p = this;
        int totalSize = 0;
        while (p != null) {
            totalSize += 1;
            p = p.rest;
        }
        return totalSize;
    }

    public static int iterGetSize(IntList list) {
        int size = 0;
        for (IntList i = list; list != null; list = list.rest) {
            size++;
        }
        return size;
    }

    public int get(int i) {
        if (i == 0) {
            return first;
        } else {
            return rest.get(i - 1);
        }
    }

    public static void main(String[] args) {
        IntList L = new IntList(15, null);
        L = new IntList(10, L);
        L = new IntList(5, L);

        System.out.println(L.size());
        System.out.println(L.iterativeSize());
        System.out.println(recGetSize(L));
        System.out.println(iterGetSize(L));
        System.out.println("-------------------------------");
        System.out.println(L.get(0));
        System.out.println(L.get(1));
        System.out.println(L.get(2));
        System.out.println("--------------------------------");
        System.out.println(L.get(0));
        System.out.println(L.get(0));
        System.out.println(L.get(0));
    }
}
