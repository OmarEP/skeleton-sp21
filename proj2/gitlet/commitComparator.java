package gitlet;

import java.util.Comparator;

public class commitComparator implements Comparator<Commit> {
    @Override
    public int compare(Commit o1, Commit o2) {
        return o1.getTimestamp().compareTo(o2.getTimestamp());
    }
}
