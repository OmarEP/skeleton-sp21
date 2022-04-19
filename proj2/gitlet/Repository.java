package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.Formatter;

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

    // Stage Repository
    private static Stage stage;



    /* TODO: fill in the rest of this class. */
    public static void initCommand() {
        if (!GITLET_DIR.exists()) {
            GITLET_DIR.mkdir();
        } else {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            System.exit(0);
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

        stage = new Stage();

        Utils.writeObject(Stage.INDEX, stage);

        Commit initialCommit = new Commit();
        File initialCommitFile = Utils.join(Commit.COMMIT_DIR,initialCommit.getHashCode());
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

        Utils.writeObject(join(Commit.COMMIT_DIR, curentCommit.getHashCode()), curentCommit);
        stage.getStageForAddition().clear();

        Utils.writeObject(Stage.INDEX, stage);

        Utils.writeObject(HEAD, curentCommit);
        Utils.writeObject(MASTER, curentCommit);
    }

    public static void checkoutCommand(String filename) {
        Commit headCommit = Utils.readObject(HEAD, Commit.class);

        if (headCommit.getBlobTreeMap().containsKey(filename)) {
            Blob blob = Utils.readObject(join(Blob.BLOB_DIR, headCommit.getBlobTreeMap().get(filename)), Blob.class);
            Utils.writeContents(join(CWD, filename), blob.getContent());
        } else {
            System.out.println("File does not exist in that commit.");
            System.exit(0);
        }
    }

    public static void checkoutCommand(String commitId, String filename) {
        if (commitId.length() < 40 && Utils.plainFilenamesIn(Commit.COMMIT_DIR) != null) {

            for (String file : Utils.plainFilenamesIn(Commit.COMMIT_DIR)) {
                if (file.startsWith(commitId)) {
                    Commit currentCommit = Utils.readObject(join(Commit.COMMIT_DIR, file), Commit.class);

                    if (currentCommit.getBlobTreeMap().containsKey(filename)) {
                        Blob blob = Utils.readObject(join(Blob.BLOB_DIR, currentCommit.getBlobTreeMap().get(filename)), Blob.class);
                        Utils.writeContents(join(CWD, filename), blob.getContent());
                    } else {
                        System.out.println("File does not exist in that commit.");
                        System.exit(0);
                    }
                }
            }
        } else if (Utils.join(Commit.COMMIT_DIR, commitId).exists()) {
                Commit currentCommit = Utils.readObject(join(Commit.COMMIT_DIR, commitId), Commit.class);

                if (currentCommit.getBlobTreeMap().containsKey(filename)) {
                    Blob blob = Utils.readObject(join(Blob.BLOB_DIR, currentCommit.getBlobTreeMap().get(filename)), Blob.class);
                    Utils.writeContents(join(CWD, filename), blob.getContent());
                } else {
                    System.out.println("File does not exist in that commit.");
                    System.exit(0);
                }
        } else {
            System.out.println("File does not exist in that commit.");
            System.exit(0);
        }
    }

    public static void logCommand() {
        StringBuilder logInfo = new StringBuilder();
        Commit currentCommit = Utils.readObject(HEAD, Commit.class);
        String lineSeparator = System.lineSeparator();

        for (; currentCommit.getFirstParentCommit() != null; currentCommit = Utils.readObject(join(Commit.COMMIT_DIR, currentCommit.getFirstParentCommit()), Commit.class)) {
            Formatter formatter = new Formatter();
            formatter.format("===" + lineSeparator);
            formatter.format("commit %s" + lineSeparator, currentCommit.getHashCode());
            // Date: Thu Nov 9 20:00:05 2017 -0800
            formatter.format("Date: %ta %tb %te %tT %tY %tz" + lineSeparator, currentCommit.getTimestamp(),currentCommit.getTimestamp(), currentCommit.getTimestamp(),currentCommit.getTimestamp(), currentCommit.getTimestamp(), currentCommit.getTimestamp());
            formatter.format(currentCommit.getMessage() + lineSeparator);
            formatter.format(lineSeparator);

            logInfo.append(formatter.toString());
        }

        Formatter formatter = new Formatter();
        formatter.format("===" + lineSeparator);
        formatter.format("commit %s" + lineSeparator, currentCommit.getHashCode());
        formatter.format("Date: %ta %tb %te %tT %tY %tz" + lineSeparator, currentCommit.getTimestamp(),currentCommit.getTimestamp(), currentCommit.getTimestamp(),currentCommit.getTimestamp(), currentCommit.getTimestamp(), currentCommit.getTimestamp());
        formatter.format(currentCommit.getMessage() + lineSeparator);
        formatter.format(lineSeparator);

        logInfo.append(formatter.toString());

        System.out.print(logInfo.toString());
    }

    public static void rmCommand(String filename) {
        stage = Utils.readObject(Stage.INDEX, Stage.class);

        stage.remove(filename);

        Utils.writeObject(Stage.INDEX, stage);
    }

    public static void globalLogCommand() {

    }
}
