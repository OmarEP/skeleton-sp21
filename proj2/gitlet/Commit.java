package gitlet;

// TODO: any imports you need here

import java.io.File;
import java.io.Serializable;
import java.util.Date;
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

    private static TreeMap<String, Blob> blobs;

    /* TODO: fill in the rest of this class. */
    public Commit() {
        this.message = "initial commit";
        this.timestamp = new Date(0);
        this.parentCommit = null;
        blobs = new TreeMap<>();
    }

    public Commit(String message, String parentCommit) {
        this.message = message;
        this.timestamp = new Date();
        this.parentCommit = parentCommit;
        blobs = new TreeMap<>();
    }
}
