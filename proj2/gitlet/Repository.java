package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static gitlet.Utils.*;

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
    public static final File CURRENT_BRANCH_NAME = join(BRANCHES, "currentBranchName.txt");

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

        if (!CURRENT_BRANCH_NAME.exists()) {
            try {
                CURRENT_BRANCH_NAME.createNewFile();
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


        Utils.writeContents(CURRENT_BRANCH_NAME, MASTER.getName());
        Utils.writeObject(join(BRANCHES, Utils.readContentsAsString(CURRENT_BRANCH_NAME)), initialCommit);
    }



    public static void addCommand(String filename) {
        stage = Utils.readObject(Stage.INDEX, Stage.class);

        stage.add(filename, HEAD);

        Utils.writeObject(Stage.INDEX, stage);
    }

    public static void commitCommand(String message) {
        stage = Utils.readObject(Stage.INDEX, Stage.class);
        if (stage.getStageForAddition().isEmpty() && stage.getStageForRemoval().isEmpty()) {
            System.out.println("No changes added to the commit.");
            System.exit(0);
        }

        Commit curentCommit = new Commit(message, HEAD, stage);

        Utils.writeObject(join(Commit.COMMIT_DIR, curentCommit.getHashCode()), curentCommit);
        stage.getStageForAddition().clear();
        stage.getStageForRemoval().clear();

        Utils.writeObject(Stage.INDEX, stage);

        Utils.writeObject(HEAD, curentCommit);
        Utils.writeObject(join(BRANCHES, Utils.readContentsAsString(CURRENT_BRANCH_NAME)), curentCommit);
    }

    public static void commitCommand(String message, Commit secondParentCommit) {
        stage = Utils.readObject(Stage.INDEX, Stage.class);
        if (stage.getStageForAddition().isEmpty() && stage.getStageForRemoval().isEmpty()) {
            System.out.println("No changes added to the commit.");
            System.exit(0);
        }

        Commit curentCommit = new Commit(message, HEAD, stage);

        curentCommit.setSecondParentCommit(secondParentCommit);

        Utils.writeObject(join(Commit.COMMIT_DIR, curentCommit.getHashCode()), curentCommit);
        stage.getStageForAddition().clear();
        stage.getStageForRemoval().clear();

        Utils.writeObject(Stage.INDEX, stage);

        Utils.writeObject(HEAD, curentCommit);
        Utils.writeObject(join(BRANCHES, Utils.readContentsAsString(CURRENT_BRANCH_NAME)), curentCommit);
    }


    public static void checkoutCommand(String branchName) {

        // If the branch name doesn't exist
        if (!join(BRANCHES, branchName).exists()) {
            System.out.println("No such branch exists.");
            System.exit(0);
        // If the branch name has already been created
        } else if (Utils.readContentsAsString(CURRENT_BRANCH_NAME).equals(branchName)) {
            System.out.println("No need to checkout the current branch.");
            System.exit(0);
        // If one of the files in the Working directory isn't tracked
        } else if (Utils.plainFilenamesIn(CWD) != null){
            Commit currentBranchHeadCommit = Utils.readObject(HEAD, Commit.class);
            for (String file : Utils.plainFilenamesIn(CWD)) {
                if (!currentBranchHeadCommit.getBlobTreeMap().containsKey(file)) {
                    System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                    System.exit(0);
                }
            }
        }

        Utils.writeContents(CURRENT_BRANCH_NAME, branchName);

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
        } else if (!Utils.join(Commit.COMMIT_DIR, commitId).exists()) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
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
            if (currentCommit.getSecondParentCommit() != null) {
                formatter.format("Merge: %s %s" + lineSeparator, currentCommit.getFirstParentCommit().substring(0, 7), currentCommit.getSecondParentCommit().substring(0, 7));
            }
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

                if (Utils.readContentsAsString(CURRENT_BRANCH_NAME).equals(file)) {
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

        formatter = new Formatter();
        formatter.format("=== Modifications Not Staged For Commit ===" + lineSeparator);
        statusInfo.append(formatter.toString());

        formatter = new Formatter();
        formatter.format(lineSeparator);

        statusInfo.append(formatter.toString());

        formatter = new Formatter();
        formatter.format("=== Untracked Files ===" + lineSeparator);
        statusInfo.append(formatter.toString());

        statusInfo.append("\n");

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
        } else if (Utils.readContentsAsString(CURRENT_BRANCH_NAME).equals(branchName)) {
            System.out.println("Cannot remove the current branch.");
            System.exit(0);
        }

        join(BRANCHES, branchName).delete();
    }

    public static void resetCommand(String commitId) {
        Commit currentBranchHeadCommit = Utils.readObject(HEAD, Commit.class);

        if (commitId.length() < 40 && Utils.plainFilenamesIn(Commit.COMMIT_DIR) != null) {

            for (String commit : Utils.plainFilenamesIn(Commit.COMMIT_DIR)) {
                if (commit.startsWith(commitId)) {
                    Commit resetCommit = Utils.readObject(join(Commit.COMMIT_DIR, commit), Commit.class);

                    for (String file : Utils.plainFilenamesIn(CWD)) {
                        if (!currentBranchHeadCommit.getBlobTreeMap().containsKey(file) && resetCommit.getBlobTreeMap().containsKey(file)) {
                            System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                            System.exit(0);
                        }
                    }

                    if (Utils.plainFilenamesIn(CWD) != null){
                        for (String file : Utils.plainFilenamesIn(CWD)) {
                            Utils.restrictedDelete(file);
                        }
                    }

                    if (resetCommit.getBlobTreeMap() != null) {
                        for (String key : resetCommit.getBlobTreeMap().keySet()) {
                            Blob blob = Utils.readObject(join(Blob.BLOB_DIR, resetCommit.getBlobTreeMap().get(key)), Blob.class);
                            Utils.writeContents(join(CWD, key), blob.getContent());
                        }
                    }
                    stage = Utils.readObject(Stage.INDEX, Stage.class);
                    stage.getStageForAddition().clear();
                    stage.getStageForRemoval().clear();

                    Utils.writeObject(Stage.INDEX, stage);

                    Utils.writeObject(join(BRANCHES, Utils.readContentsAsString(CURRENT_BRANCH_NAME)), resetCommit);
                    Utils.writeObject(HEAD, resetCommit);
                    System.exit(0);
                }
            }
        } else if (Utils.join(Commit.COMMIT_DIR, commitId).exists()) {
            Commit resetCommit = Utils.readObject(join(Commit.COMMIT_DIR, commitId), Commit.class);

            for (String file : Utils.plainFilenamesIn(CWD)) {
                if (!currentBranchHeadCommit.getBlobTreeMap().containsKey(file) && resetCommit.getBlobTreeMap().containsKey(file)) {
                    System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                    System.exit(0);
                }
            }

            if (Utils.plainFilenamesIn(CWD) != null){
                for (String file : Utils.plainFilenamesIn(CWD)) {
                    Utils.restrictedDelete(file);
                }
            }

            if (resetCommit.getBlobTreeMap() != null) {
                for (String key : resetCommit.getBlobTreeMap().keySet()) {
                    Blob blob = Utils.readObject(join(Blob.BLOB_DIR, resetCommit.getBlobTreeMap().get(key)), Blob.class);
                    Utils.writeContents(join(CWD, key), blob.getContent());
                }
            }

            stage = Utils.readObject(Stage.INDEX, Stage.class);
            stage.getStageForAddition().clear();
            stage.getStageForRemoval().clear();

            Utils.writeObject(Stage.INDEX, stage);

            Utils.writeObject(join(BRANCHES, Utils.readContentsAsString(CURRENT_BRANCH_NAME)), resetCommit);
            Utils.writeObject(HEAD, resetCommit);
        } else {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }
    }

    public static void mergeCommand(String branchName) {
        stage = Utils.readObject(Stage.INDEX, Stage.class);

        if (!stage.getStageForAddition().isEmpty() || !stage.getStageForRemoval().isEmpty()) {
            System.out.println("You have uncommitted changes.");
            System.exit(0);
        }

        if (!join(BRANCHES, branchName).exists()) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        } else if (branchName.equals(readContentsAsString(CURRENT_BRANCH_NAME))) {
            System.out.println("Cannot merge a branch with itself.");
            System.exit(0);
        }

        Commit currentBranchCommit = Utils.readObject(join(BRANCHES,Utils.readContentsAsString(CURRENT_BRANCH_NAME)), Commit.class);

        Commit givenBranchCommit = Utils.readObject(join(BRANCHES,branchName), Commit.class);

        Commit splitPointCommit = findSplitPoint(branchName);

        Set<String> setOfFiles = unionOfFilesBetweenCommits(splitPointCommit, branchName);

        for (String file : Utils.plainFilenamesIn(CWD)) {
//            if (!currentBranchCommit.getBlobTreeMap().containsKey(file) && !splitPointCommit.getBlobTreeMap().containsKey(file) && !givenBranchCommit.getBlobTreeMap().containsKey(file)) {
//                System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
//                System.exit(0);
//            }

            if (currentBranchCommit.getBlobTreeMap().containsKey(file)) {
                Blob currentBranchBlob = Utils.readObject(join(Blob.BLOB_DIR, currentBranchCommit.getBlobTreeMap().get(file)), Blob.class);

                if (!currentBranchBlob.getHashCode().equals(sha1(Utils.readContentsAsString(join(CWD, file))))) {
                    System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                    System.exit(0);
                }
            } else  {
                System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                System.exit(0);
            }
        }

        // Loop through the files between the 3 commits(HEAD, OTHER, SPLIT)
        for (String file : setOfFiles) {

            // Modified in OTHER but not HEAD --> OTHER
            if (splitPointCommit.getBlobTreeMap().containsKey(file) && currentBranchCommit.getBlobTreeMap().containsKey(file) && splitPointCommit.getBlobTreeMap().get(file).equals(currentBranchCommit.getBlobTreeMap().get(file))
                    && (givenBranchCommit.getBlobTreeMap().containsKey(file) && !(givenBranchCommit.getBlobTreeMap().get(file).equals(splitPointCommit.getBlobTreeMap().get(file))))) {
                if (!join(CWD, file).exists()) {
                    try {
                        join(CWD, file).createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                checkoutCommand(givenBranchCommit.getHashCode(), file, "--");
                addCommand(file);

            // Modified in HEAD but not OTHER --> HEAD
            } else if(splitPointCommit.getBlobTreeMap().containsKey(file) && givenBranchCommit.getBlobTreeMap().containsKey(file) && splitPointCommit.getBlobTreeMap().get(file).equals(givenBranchCommit.getBlobTreeMap().get(file))
                    && (currentBranchCommit.getBlobTreeMap().containsKey(file) && !(currentBranchCommit.getBlobTreeMap().get(file).equals(splitPointCommit.getBlobTreeMap().get(file))))) {
                if (!join(CWD, file).exists()) {
                    try {
                        join(CWD, file).createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                checkoutCommand(currentBranchCommit.getHashCode(), file, "--");

            /*
                Any files that have been modified in both the current and given branch in the same way (i.e., both files now have the same content or were both removed)
                are left unchanged by the merge. If a file was removed from both the current and given branch, but a file of the same name is present in the working directory,
                it is left alone and continues to be absent (not tracked nor staged) in the merge.

             */
            } else if (splitPointCommit.getBlobTreeMap().containsKey(file)
                    && ((currentBranchCommit.getBlobTreeMap().containsKey(file) && givenBranchCommit.getBlobTreeMap().containsKey(file)
                    && currentBranchCommit.getBlobTreeMap().get(file).equals(givenBranchCommit.getBlobTreeMap().get(file)) && !splitPointCommit.getBlobTreeMap().get(file).equals(currentBranchCommit.getBlobTreeMap().get(file)))
                    || !currentBranchCommit.getBlobTreeMap().containsKey(file) && !givenBranchCommit.getBlobTreeMap().containsKey(file))) {
                continue;

            // Not in SPLIT nor OTHER but in HEAD --> HEAD
            } else if (!splitPointCommit.getBlobTreeMap().containsKey(file) && !givenBranchCommit.getBlobTreeMap().containsKey(file) && currentBranchCommit.getBlobTreeMap().containsKey(file)) {
                checkoutCommand(currentBranchCommit.getHashCode(), file, "--");

            // Not in SPLIT nor HEAD but in OTHER --> OTHER
            } else if (!splitPointCommit.getBlobTreeMap().containsKey(file) && !currentBranchCommit.getBlobTreeMap().containsKey(file) && givenBranchCommit.getBlobTreeMap().containsKey(file)) {
                if (!join(CWD, file).exists()) {
                    try {
                        join(CWD, file).createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                checkoutCommand(givenBranchCommit.getHashCode(), file, "--");
                addCommand(file);

            // Unmodified in HEAD but not present in OTHER --> REMOVE
            } else if (splitPointCommit.getBlobTreeMap().containsKey(file) && currentBranchCommit.getBlobTreeMap().containsKey(file) && splitPointCommit.getBlobTreeMap().get(file).equals(currentBranchCommit.getBlobTreeMap().get(file)) &&
                                        !givenBranchCommit.getBlobTreeMap().containsKey(file)) {
                rmCommand(file);

            // Unmodified in OTHER but not present in HEAD --> Remain REMOVED
            } else if (splitPointCommit.getBlobTreeMap().containsKey(file) && givenBranchCommit.getBlobTreeMap().containsKey(file) && splitPointCommit.getBlobTreeMap().get(file).equals(givenBranchCommit.getBlobTreeMap().get(file)) &&
                    !currentBranchCommit.getBlobTreeMap().containsKey(file)) {
                continue;

            // Modified in OTHER and HEAD --> In same way --> DNM(same)
            //                            --> In diff ways --> Conflict
//          }   else if ( (splitPointCommit.getBlobTreeMap().containsKey(file) && currentBranchCommit.getBlobTreeMap().containsKey(file) && givenBranchCommit.getBlobTreeMap().containsKey(file)
//                    && !(splitPointCommit.getBlobTreeMap().get(file).equals(currentBranchCommit.getBlobTreeMap().get(file))) && !(splitPointCommit.getBlobTreeMap().get(file).equals(givenBranchCommit.getBlobTreeMap().get(file))))
//                    || ((splitPointCommit.getBlobTreeMap().containsKey(file)) && ((!currentBranchCommit.getBlobTreeMap().containsKey(file) || !splitPointCommit.getBlobTreeMap().get(file).equals(currentBranchCommit.getBlobTreeMap().get(file)))
//                    || (!givenBranchCommit.getBlobTreeMap().containsKey(file) || !splitPointCommit.getBlobTreeMap().get(file).equals(givenBranchCommit.getBlobTreeMap().get(file)))))
//                    || (!splitPointCommit.getBlobTreeMap().containsKey(file) && currentBranchCommit.getBlobTreeMap().containsKey(file) && givenBranchCommit.getBlobTreeMap().containsKey(file)
//            )           && !currentBranchCommit.getBlobTreeMap().get(file).equals(givenBranchCommit.getBlobTreeMap().get(file))) {
//                if (currentBranchCommit.getBlobTreeMap().get(file).equals(givenBranchCommit.getBlobTreeMap().get(file))) {
//                    checkoutCommand(currentBranchCommit.getHashCode(), file, "--");
//                } else {
//                    mergeConflict(currentBranchCommit, givenBranchCommit, file);
//                    addCommand(file);
//                }
            } else {
                mergeConflict(currentBranchCommit, givenBranchCommit, file);
            }

        }
        commitCommand("Merged " + branchName + " into " + Utils.readContentsAsString(CURRENT_BRANCH_NAME) + ".", givenBranchCommit);
    }

    private static void mergeConflict(Commit currentBranchCommit, Commit givenBranchCommit, String fileName) {
        StringBuilder mergedFileContent = new StringBuilder();

        System.out.println("Encountered a merge conflict.");

        if (currentBranchCommit.getBlobTreeMap().containsKey(fileName) && givenBranchCommit.getBlobTreeMap().containsKey(fileName)
                && !currentBranchCommit.getBlobTreeMap().get(fileName).equals(givenBranchCommit.getBlobTreeMap().get(fileName))) {
            if (!join(CWD, fileName).exists()) {
                try {
                    join(CWD, fileName).createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Blob currentBranchBlob = Utils.readObject(join(Blob.BLOB_DIR, currentBranchCommit.getBlobTreeMap().get(fileName)), Blob.class);
            Blob givenBranchBlob = Utils.readObject(join(Blob.BLOB_DIR, givenBranchCommit.getBlobTreeMap().get(fileName)), Blob.class);

//            formatter.format("<<<<<<< HEAD" + lineSeparator);
//            formatter.format(currentBranchBlob.getContent());
//            formatter.format("=======" + lineSeparator);
//            formatter.format(givenBranchBlob.getContent());
//            formatter.format(">>>>>>>" + lineSeparator);
            mergedFileContent.append("<<<<<<< HEAD\n");
            mergedFileContent.append(currentBranchBlob.getContent());
            mergedFileContent.append("=======\n");
            mergedFileContent.append(givenBranchBlob.getContent());
            mergedFileContent.append(">>>>>>>\n");
            Utils.writeContents(join(CWD, fileName), mergedFileContent.toString());
            addCommand(fileName);
        } else if (currentBranchCommit.getBlobTreeMap().containsKey(fileName) && !givenBranchCommit.getBlobTreeMap().containsKey(fileName)) {
            if (!join(CWD, fileName).exists()) {
                try {
                    join(CWD, fileName).createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            Blob currentBranchBlob = Utils.readObject(join(Blob.BLOB_DIR, currentBranchCommit.getBlobTreeMap().get(fileName)), Blob.class);

//            formatter.format("<<<<<<< HEAD" + lineSeparator);
//            formatter.format(currentBranchBlob.getContent());
//            formatter.format("=======" + lineSeparator);
//            // formatter.format(lineSeparator);
//            formatter.format(">>>>>>>");
//            formatter.format("\n");
            mergedFileContent.append("<<<<<<< HEAD\n");
            mergedFileContent.append(currentBranchBlob.getContent());
            mergedFileContent.append("=======\n");
            // mergedFileContent.append(givenBranchBlob.getContent());
            mergedFileContent.append(">>>>>>>\n");

            Utils.writeContents(join(CWD, fileName), mergedFileContent.toString());
            addCommand(fileName);
        } else if (!currentBranchCommit.getBlobTreeMap().containsKey(fileName) && givenBranchCommit.getSecondParentCommit().contains(fileName)) {
            if (!join(CWD, fileName).exists()) {
                try {
                    join(CWD, fileName).createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            Blob givenBranchBlob = Utils.readObject(join(Blob.BLOB_DIR, givenBranchCommit.getBlobTreeMap().get(fileName)), Blob.class);

//            formatter.format("<<<<<<< HEAD");
//            formatter.format("\n");
//            formatter.format("=======" + lineSeparator);
//            formatter.format(givenBranchBlob.getContent());
//            formatter.format(">>>>>>>");
//            formatter.format("\n");
            mergedFileContent.append("<<<<<<< HEAD\n");
            // mergedFileContent.append(currentBranchBlob.getContent());
            mergedFileContent.append("=======\n");
            mergedFileContent.append(givenBranchBlob.getContent());
            mergedFileContent.append(">>>>>>>\n");

            Utils.writeContents(join(CWD, fileName), mergedFileContent.toString());
            addCommand(fileName);
        } else {
            if (!join(CWD, fileName).exists()) {
                try {
                    join(CWD, fileName).createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
//            formatter.format("<<<<<<< HEAD" + lineSeparator);
//            // formatter.format(lineSeparator);
//            formatter.format("=======" + lineSeparator);
//            // formatter.format(lineSeparator);
//            formatter.format(">>>>>>>");
//            formatter.format("\n");
            mergedFileContent.append("<<<<<<< HEAD\n");
            // mergedFileContent.append(currentBranchBlob.getContent());
            mergedFileContent.append("=======\n");
            // mergedFileContent.append(givenBranchBlob.getContent());
            mergedFileContent.append(">>>>>>>\n");
            Utils.writeContents(join(CWD, fileName), mergedFileContent.toString());
            addCommand(fileName);
        }
    }

    private static Commit findSplitPoint(String branchName) {
        ArrayList<String> currentBranchAncestorCommitsList = new ArrayList<>();
        Commit currentBranchCommit = Utils.readObject(join(BRANCHES,Utils.readContentsAsString(CURRENT_BRANCH_NAME)), Commit.class);
        for (; currentBranchCommit.getFirstParentCommit() != null; currentBranchCommit = Utils.readObject(join(Commit.COMMIT_DIR, currentBranchCommit.getFirstParentCommit()), Commit.class)) {
            currentBranchAncestorCommitsList.add(currentBranchCommit.getHashCode());
        }
        currentBranchAncestorCommitsList.add(currentBranchCommit.getHashCode());

        currentBranchCommit = Utils.readObject(join(BRANCHES,Utils.readContentsAsString(CURRENT_BRANCH_NAME)), Commit.class);
        if (currentBranchCommit.getSecondParentCommit() != null) {
            for (; currentBranchCommit.getSecondParentCommit() != null; currentBranchCommit = Utils.readObject(join(Commit.COMMIT_DIR, currentBranchCommit.getSecondParentCommit()), Commit.class)) {
                currentBranchAncestorCommitsList.add(currentBranchCommit.getHashCode());
            }
            currentBranchAncestorCommitsList.add(currentBranchCommit.getHashCode());
        }

        ArrayList<String> givenBranchAncestorCommitsList = new ArrayList<>();
        Commit givenBranchCommit = Utils.readObject(join(BRANCHES,branchName), Commit.class);
        for (; givenBranchCommit.getFirstParentCommit() != null; givenBranchCommit = Utils.readObject(join(Commit.COMMIT_DIR, givenBranchCommit.getFirstParentCommit()), Commit.class)) {
            givenBranchAncestorCommitsList.add(givenBranchCommit.getHashCode());
        }
        givenBranchAncestorCommitsList.add(givenBranchCommit.getHashCode());

        givenBranchCommit = Utils.readObject(join(BRANCHES,branchName), Commit.class);
        if (givenBranchCommit.getSecondParentCommit() != null) {
            for (; givenBranchCommit.getSecondParentCommit() != null; givenBranchCommit = Utils.readObject(join(Commit.COMMIT_DIR, givenBranchCommit.getSecondParentCommit()), Commit.class)) {
                givenBranchAncestorCommitsList.add(givenBranchCommit.getHashCode());
            }
            givenBranchAncestorCommitsList.add(givenBranchCommit.getHashCode());
        }

        List<String> result = currentBranchAncestorCommitsList.stream()
                .distinct()
                .filter(givenBranchAncestorCommitsList::contains)
                .collect(Collectors.toList());

        ArrayList<Commit> ancestorCommits = new ArrayList<>();
        for (String current : result) {
            if (current != null) {
                ancestorCommits.add(Utils.readObject(join(Commit.COMMIT_DIR, current), Commit.class));
            }
        }

        ancestorCommits.sort(new commitComparator().reversed());
        Commit splitPointCommit = ancestorCommits.get(0);
        // splitPointCommit.dump();

        currentBranchCommit = Utils.readObject(join(BRANCHES,Utils.readContentsAsString(CURRENT_BRANCH_NAME)), Commit.class);
        givenBranchCommit = Utils.readObject(join(BRANCHES,branchName), Commit.class);
        if (givenBranchCommit.getHashCode().equals(splitPointCommit.getHashCode())) {
            System.out.println("Given branch is an ancestor of the current branch.");
            System.exit(0);
        } else if (currentBranchCommit.getHashCode().equals(splitPointCommit.getHashCode())) {
            checkoutCommand(branchName);
            System.out.println("Current branch fast-forwarded.");
            System.exit(0);
        }

        return splitPointCommit;
    }

    private static Set<String> unionOfFilesBetweenCommits(Commit splitPointCommit, String branchName) {

        Set<String> setOfFiles = new HashSet<>();

        Commit givenBranchCommit = Utils.readObject(join(BRANCHES,branchName), Commit.class);

        Commit currentBranchCommit = Utils.readObject(join(BRANCHES,Utils.readContentsAsString(CURRENT_BRANCH_NAME)), Commit.class);

        setOfFiles.addAll(splitPointCommit.getBlobTreeMap().keySet());
        setOfFiles.addAll(givenBranchCommit.getBlobTreeMap().keySet());
        setOfFiles.addAll(currentBranchCommit.getBlobTreeMap().keySet());

        return setOfFiles;
    }
}
