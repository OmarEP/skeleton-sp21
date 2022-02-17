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

    public void addFirst(int x) {
        IntList L = this;
        IntList newNode = new IntList(x, null);
        newNode.rest = L;
        L = newNode;
    }

    public static IntList incrList(IntList L, int x) {
//        if (L == null) {
//            return null;
//        }
//        IntList N = new IntList(L.first + x, L.rest);
//        IntList q = N;
//        IntList p = L.rest;
//        while (p != null) {
//            q.rest = new IntList(p.first + x, p.rest);
//            q = q.rest;
//            p = p.rest;
//        }
        if (L == null) {
            return null;
        } else {
            return new IntList(L.first + x, incrList(L.rest, x));
        }
    }

    /**
     * Returns an IntList identical to L, but with
     * each element incremented by x. Not allowed to use
     * the 'new' keyword.
     */
    public static IntList dincrList(IntList L, int x) {
        /* Your code here. */
//        IntList p = L;
//        while (p != null) {
//            p.first = p.first + x;
//            p = p.rest;
//        }
//        return L;
        if (L == null) {
            return null;
        } else {
            L.first = L.first + x;
            dincrList(L.rest, x);
            return L;
        }
    }

    public static void main(String[] args) {
        IntList L = new IntList(15, null);
        L = new IntList(10, L);
        L = new IntList(5, L);
        System.out.println(L.get(0));
        System.out.println(L.get(1));
        System.out.println(L.get(2));
        System.out.println("---------------------------------");
        IntList M = incrList(L, 3);
        System.out.println(M.get(0));
        System.out.println(M.get(1));
        System.out.println(M.get(2));
        System.out.println("---------------------------------");
//        IntList V = dincrList(L, 3);
//        System.out.println(V.get(0));
//        System.out.println(V.get(1));
//        System.out.println(V.get(2));
        System.out.println("---------------------------------");
        L.addFirst(3);
        System.out.println(L.get(0));
        System.out.println(L.get(1));
        System.out.println(L.get(2));
        System.out.println(L.get(3));
    }
}
