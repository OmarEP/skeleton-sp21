package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.TreeMap;

import static gitlet.Utils.join;
import static gitlet.Utils.sha1;

public class Stage implements Serializable {

    // TreeMap for staging for addition area
    private TreeMap<String, Blob> stageForAddition;

    // TreeMap for removal for addition area
    private TreeMap<String, Blob> stageForRemoval;

    // File to keep track of all the serialized objects in the staging area
    public final static File INDEX = join(Repository.GITLET_DIR, "INDEX");

    public Stage() {
        stageForAddition = new TreeMap<String, Blob>();
        stageForRemoval = new TreeMap<String, Blob>();
    }

    public void add(String filename) {

        Blob blob = new Blob(filename);

        File file = join(Repository.CWD, filename);
        String contents = Utils.readContentsAsString(file);

        Utils.writeObject(join(Blob.BLOB_DIR, sha1(contents)), blob);

        stageForAddition.put(filename, blob);
    }

    public void remove(String filename) {

    }
}
