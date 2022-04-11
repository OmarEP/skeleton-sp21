package gitlet;

// TODO: any imports you need here

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

    private String parentCommit;

    private static TreeMap<String, String> blobs;

    /* TODO: fill in the rest of this class. */
    public Commit() {
        this.message = "initial commit";
        this.timestamp = new Date(0);
        this.parentCommit = null;
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

        this.message = message;
        this.timestamp = new Date();
        this.parentCommit = Utils.sha1(parentCommit.getTimestamp().toString(), parentCommit.getMessage(), Objects.toString(parentCommit.getParentCommit()));
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

    public String getParentCommit() {
        return parentCommit;
    }

    public TreeMap<String, String> getBlobTreeMap() {
        return blobs;
    }
}
