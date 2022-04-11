package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import static gitlet.Utils.join;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));

    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");

    // The branches folder
    public static final File BRANCHES = join(GITLET_DIR, "branches");

    // The master branch
    public static final File MASTER = join(BRANCHES, "master");

    // The HEAD pointer
    public static final File HEAD = join(GITLET_DIR, "HEAD");

    // The Commit's blob treemap's file
    public static final File COMMIT_INFO = join(GITLET_DIR, "COMMIT_INFO");

    // Stage Repository
    private static Stage stage;



    /* TODO: fill in the rest of this class. */
    public static void initCommand() {
        if (!GITLET_DIR.exists()) {
            GITLET_DIR.mkdir();
        }

        if (!Commit.COMMIT_DIR.exists()) {
            Commit.COMMIT_DIR.mkdir();
        }

        if (!Blob.BLOB_DIR.exists()) {
            Blob.BLOB_DIR.mkdir();
        }

        if (!BRANCHES.exists()) {
            BRANCHES.mkdir();
        }

        if (!MASTER.exists()) {
            try {
                MASTER.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (!HEAD.exists()) {
            try {
                HEAD.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (!Stage.INDEX.exists()) {
            try {
                Stage.INDEX.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (!COMMIT_INFO.exists()) {
            try {
                COMMIT_INFO.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        stage = new Stage();

        Utils.writeObject(Stage.INDEX, stage);

        Commit initialCommit = new Commit();
        File initialCommitFile = Utils.join(Commit.COMMIT_DIR, Utils.sha1(initialCommit.getMessage(), initialCommit.getTimestamp().toString(), Objects.toString(initialCommit.getParentCommit())));
        Utils.writeObject(initialCommitFile, initialCommit);

        Utils.writeObject(HEAD, initialCommit);
        Utils.writeObject(MASTER, initialCommit);
    }



    public static void addCommand(String filename) {
        stage = Utils.readObject(Stage.INDEX, Stage.class);

        stage.add(filename, HEAD);

        Utils.writeObject(Stage.INDEX, stage);
    }

    public static void commitCommand(String message) {
        stage = Utils.readObject(Stage.INDEX, Stage.class);

        Commit curentCommit = new Commit(message, HEAD, stage);

        Utils.writeObject(join(Commit.COMMIT_DIR, Utils.sha1(Objects.toString(curentCommit.getParentCommit()), curentCommit.getMessage(), curentCommit.getTimestamp().toString())), curentCommit);
        stage.getStageForAddition().clear();

        Utils.writeObject(Stage.INDEX, stage);

        Utils.writeObject(HEAD, curentCommit);
        Utils.writeObject(MASTER, curentCommit);
    }
}
