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

    // The current Branch name
    public static final File currentBranchName = join(BRANCHES, "currentBranchName.txt");

    // Stage Repository
    private static Stage stage;


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

        if (!currentBranchName.exists()) {
            try {
                currentBranchName.createNewFile();
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


        Utils.writeContents(currentBranchName, MASTER.getName());
        Utils.writeObject(join(BRANCHES, Utils.readContentsAsString(currentBranchName)), initialCommit);
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
        stage.getStageForRemoval().clear();

        Utils.writeObject(Stage.INDEX, stage);

        Utils.writeObject(HEAD, curentCommit);
        Utils.writeObject(join(BRANCHES, Utils.readContentsAsString(currentBranchName)), curentCommit);
    }

    public static void checkoutCommand(String branchName) {

        // If the branch name doesn't exist
        if (!join(BRANCHES, branchName).exists()) {
            System.out.println("No such branch exists.");
            System.exit(0);
        // If the branch name has already been created
        } else if (Utils.readContentsAsString(currentBranchName).equals(branchName)) {
            System.out.println("No need to checkout the current branch.");
            System.exit(0);
        // If one of the files in the Working directory isn't tracked
        } else if (Utils.plainFilenamesIn(CWD) != null){
            Commit currentBranchHeadCommit = Utils.readObject(HEAD, Commit.class);
            for (String file : Utils.plainFilenamesIn(CWD)) {
                if (!currentBranchHeadCommit.getBlobTreeMap().containsKey(file)) {
                    System.out.println(" There is an untracked file in the way; delete it, or add and commit it first.");
                    System.exit(0);
                }
            }
        }

        Utils.writeContents(currentBranchName, branchName);

        if (Utils.plainFilenamesIn(CWD) != null){
            for (String file : Utils.plainFilenamesIn(CWD)) {
                Utils.restrictedDelete(file);
            }
        }

        Commit currentBranchHeadCommit = Utils.readObject(join(BRANCHES, branchName), Commit.class);
        if (currentBranchHeadCommit.getBlobTreeMap() != null) {
            for (String key : currentBranchHeadCommit.getBlobTreeMap().keySet()) {
                Blob blob = Utils.readObject(join(Blob.BLOB_DIR, currentBranchHeadCommit.getBlobTreeMap().get(key)), Blob.class);
                Utils.writeContents(join(CWD, key), blob.getContent());
            }
        }

        Utils.writeObject(HEAD, currentBranchHeadCommit);
    }

    public static void checkoutCommand(String filename, String placeHolder) {
        Commit headCommit = Utils.readObject(HEAD, Commit.class);

        if (headCommit.getBlobTreeMap().containsKey(filename)) {
            Blob blob = Utils.readObject(join(Blob.BLOB_DIR, headCommit.getBlobTreeMap().get(filename)), Blob.class);
            Utils.writeContents(join(CWD, filename), blob.getContent());
        } else {
            System.out.println("File does not exist in that commit.");
            System.exit(0);
        }
    }

    public static void checkoutCommand(String commitId, String filename, String placeHolder) {
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
        StringBuilder logInfo = new StringBuilder();
        String lineSeparator = System.lineSeparator();

        if (Utils.plainFilenamesIn(Commit.COMMIT_DIR) != null) {
            for (String file : Utils.plainFilenamesIn(Commit.COMMIT_DIR)) {
                Commit currentCommit = Utils.readObject(join(Commit.COMMIT_DIR, file), Commit.class);

                Formatter formatter = new Formatter();
                formatter.format("===" + lineSeparator);
                formatter.format("commit %s" + lineSeparator, currentCommit.getHashCode());
                // Date: Thu Nov 9 20:00:05 2017 -0800
                formatter.format("Date: %ta %tb %te %tT %tY %tz" + lineSeparator, currentCommit.getTimestamp(),currentCommit.getTimestamp(), currentCommit.getTimestamp(),currentCommit.getTimestamp(), currentCommit.getTimestamp(), currentCommit.getTimestamp());
                formatter.format(currentCommit.getMessage() + lineSeparator);
                formatter.format(lineSeparator);

                logInfo.append(formatter.toString());
            }
        }

        System.out.print(logInfo.toString());
    }

    public static void findCommand(String commitMessage) {
        StringBuilder logInfo = new StringBuilder();
        String lineSeparator = System.lineSeparator();

        if (Utils.plainFilenamesIn(Commit.COMMIT_DIR) != null) {
            for (String file : Utils.plainFilenamesIn(Commit.COMMIT_DIR)) {
                Commit currentCommit = Utils.readObject(join(Commit.COMMIT_DIR, file), Commit.class);

                Formatter formatter = new Formatter();
                if (currentCommit.getMessage().equals(commitMessage)) {
                    formatter.format(currentCommit.getHashCode() + lineSeparator);
                    logInfo.append(formatter.toString());
                }
            }
        }
        if (logInfo.length() == 0) {
            System.out.println("Found no commit with that message.");
            System.exit(0);
        }
        System.out.print(logInfo.toString());
    }

    public static void statusCommand() {
        Formatter formatter = new Formatter();
        StringBuilder statusInfo = new StringBuilder();
        String lineSeparator = System.lineSeparator();

        formatter.format("=== Branches ===" + lineSeparator);
        if (Utils.plainFilenamesIn(BRANCHES) != null) {
            for (String file : Utils.plainFilenamesIn(BRANCHES)) {
                if (file.equals("currentBranchName.txt")) {
                    continue;
                }

                if (Utils.readContentsAsString(currentBranchName).equals(file)) {
                    formatter.format("*" + file + lineSeparator);
                } else {
                    formatter.format(file + lineSeparator);
                }
            }
        }
        formatter.format(lineSeparator);
        statusInfo.append(formatter.toString());

        formatter = new Formatter();
        formatter.format("=== Staged Files ===" + lineSeparator);
        statusInfo.append(formatter.toString());

        Stage tempStage = Utils.readObject(Stage.INDEX, Stage.class);
        if (tempStage.getStageForAddition() != null) {
            for (String file : tempStage.getStageForAddition().keySet()) {
                formatter = new Formatter();
                formatter.format(file + lineSeparator);
                statusInfo.append(formatter.toString());
            }
        }
        formatter = new Formatter();
        formatter.format(lineSeparator);

        statusInfo.append(formatter.toString());

        formatter = new Formatter();
        formatter.format("=== Removed Files ===" + lineSeparator);
        statusInfo.append(formatter.toString());

        if (tempStage.getStageForRemoval() != null) {
            for (String file : tempStage.getStageForRemoval().keySet()) {
                formatter = new Formatter();
                formatter.format(file + lineSeparator);
                statusInfo.append(formatter.toString());
            }
        }
        formatter = new Formatter();
        formatter.format(lineSeparator);

        statusInfo.append(formatter.toString());

        System.out.print(statusInfo.toString());
    }

    public static void branchCommand(String branchName) {
        if (join(BRANCHES, branchName).exists()) {
            System.out.println("A branch with that name already exists.");
            System.exit(0);
        }

        Commit currentCommit = Utils.readObject(HEAD, Commit.class);
        Utils.writeObject(join(BRANCHES, branchName), currentCommit);
    }

    public static void removebranchCommand(String branchName) {
        if (!join(BRANCHES, branchName).exists()) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        } else if (Utils.readContentsAsString(currentBranchName).equals(branchName)) {
            System.out.println("Cannot remove the current branch.");
            System.exit(0);
        }

        join(BRANCHES, branchName).delete();
    }
}
