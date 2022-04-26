package gitlet;


import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.TreeMap;

import static gitlet.Utils.join;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Commit implements Serializable {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */
    // The commits directory
    public static final File COMMIT_DIR = join(Repository.GITLET_DIR, "commits");

    /** The message of this Commit. */
    private String message;

    private Date timestamp;

    private String firstParentCommit;

    private String secondParentCommit;

    private TreeMap<String, String> blobs;

    /* TODO: fill in the rest of this class. */
    public Commit() {
        this.message = "initial commit";
        this.timestamp = new Date(0);
        this.firstParentCommit = null;
        this.secondParentCommit = null;
        blobs = new TreeMap<>();
    }

    public Commit(String message, File headCommit, Stage stagingArea) {
        Commit parentCommit = Utils.readObject(Repository.HEAD, Commit.class);
        blobs = new TreeMap<>();
        blobs.putAll(parentCommit.getBlobTreeMap());

        TreeMap<String, String> tempStagingArea = stagingArea.getStageForAddition();
        for (String key : tempStagingArea.keySet()) {
            if (blobs.containsKey(key)) {
                if (!isSameBlob(blobs.get(key), tempStagingArea.get(key))) {
                    blobs.put(key, tempStagingArea.get(key));
                }
            } else {
                blobs.put(key, tempStagingArea.get(key));
            }
        }

        TreeMap<String, File> tempRemovalArea = stagingArea.getStageForRemoval();
        for (String key : tempRemovalArea.keySet()) {
            if (blobs.containsKey(key)) {
                blobs.remove(key);
            }
        }

        this.message = message;
        this.timestamp = new Date();
        this.firstParentCommit = parentCommit.getHashCode();
    }

    private boolean isSameBlob(String firstBlob, String secondBlob) {
        return firstBlob.equals(secondBlob);
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getFirstParentCommit() {
        return firstParentCommit;
    }

    public String getSecondParentCommit() {
        return secondParentCommit;
    }

    public TreeMap<String, String> getBlobTreeMap() {
        return blobs;
    }

    public String getHashCode() {
        return Utils.sha1(this.getTimestamp().toString(), this.getMessage(), Objects.toString(this.getFirstParentCommit()), Objects.toString(this.secondParentCommit));
    }
}
