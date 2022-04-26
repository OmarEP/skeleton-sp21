package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.TreeMap;

import static gitlet.Utils.join;

public class Stage implements Serializable {

    // TreeMap for staging for addition area
    private TreeMap<String, String> stageForAddition;

    // TreeMap for removal for addition area
    private TreeMap<String, File> stageForRemoval;

    // File to keep track of all the serialized objects in the staging area
    public final static File INDEX = join(Repository.GITLET_DIR, "INDEX");

    public Stage() {
        stageForAddition = new TreeMap<String, String>();
        stageForRemoval = new TreeMap<String, File>();
    }

    public void add(String filename, File HEAD) {
        // Read the current head commit into an object
        Commit headCommit = Utils.readObject(HEAD, Commit.class);

        // Create a blob from the current filename
        Blob blob = new Blob(filename);

        // Blob treemap from head commit
        TreeMap<String, String> headCommitBlobTreeMap = headCommit.getBlobTreeMap();

        // If the current stageForAddition treemap contains the filename as a key, then we replace the previous
        // blob with the new one.
        if (stageForAddition.containsKey(filename) && headCommitBlobTreeMap != null && !headCommitBlobTreeMap.containsKey(filename)) {
            Utils.writeObject(join(Blob.BLOB_DIR, blob.getHashCode()), blob);

            stageForAddition.put(filename, blob.getHashCode());
        /*
            If the current working version of the file is identical to the version in the current commit, do not stage it to be added,
            and remove it from the staging area if it is already there (as can happen when a file is changed, added, and then changed
            back to it’s original version).
         */
        } else if (stageForAddition.containsKey(filename) && headCommitBlobTreeMap != null && headCommitBlobTreeMap.containsKey(filename) && isSameBlob(blob.getHashCode(), headCommitBlobTreeMap.get(filename))) {
            stageForAddition.remove(filename);

        // if the head commit's blob treemap contains the filename as a key and the contents of both blobs are different.
        } else if (headCommitBlobTreeMap != null && headCommitBlobTreeMap.containsKey(filename) && !isSameBlob(blob.getHashCode(), headCommitBlobTreeMap.get(filename))) {
                Utils.writeObject(join(Blob.BLOB_DIR, blob.getHashCode()), blob);

                stageForAddition.put(filename, blob.getHashCode());

        // Otherwise, just add the current blob into the stageForAddition treemap
        } else {
            Utils.writeObject(join(Blob.BLOB_DIR, blob.getHashCode()), blob);

            stageForAddition.put(filename, blob.getHashCode());
        }
    }

    private boolean isSameBlob(String firstBlob, String secondBlob) {
        return firstBlob.equals(secondBlob);
    }

    public TreeMap<String, String> getStageForAddition() {
        return stageForAddition;
    }

    public void remove(String filename) {
        Commit headCommit = Utils.readObject(Repository.HEAD, Commit.class);
        StringBuilder listRemovedFiles = new StringBuilder();

        if (this.getStageForAddition() != null && this.getStageForAddition().containsKey(filename)) {
            this.getStageForAddition().remove(filename);
        } else if (headCommit.getBlobTreeMap() != null && headCommit.getBlobTreeMap().containsKey(filename)) {
            this.getStageForRemoval().put(filename, join(Repository.CWD, filename));

            if(!Utils.join(Repository.REMOVED_FILES, filename).exists()) {
                try {
                    Utils.join(Repository.REMOVED_FILES, filename).createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (this.getStageForRemoval().get(filename).exists()) {
                Utils.restrictedDelete(this.getStageForRemoval().get(filename));
            }
        } else {
            System.out.println("No reason to remove the file.");
        }
    }

    public TreeMap<String, File> getStageForRemoval() {
        return stageForRemoval;
    }
}
