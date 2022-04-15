# Gitlet Design Document

**Name**:

## Classes and Data Structures

### Repository

#### Fields

1. Commit Folder: in order to keep track of the current commits in the repository 
2. Blob Folder: in order to keep track of all the blobs(file info)
3. Branches folder to hold the MASTER branch and any subsequent branches.
4. Text file with the SHA1 of the HEAD pointer: A way to remember the current HEAD pointer

### Stage

#### Fields

1. Tree Map for "Staged for addiction" area: Quick way to store to the staging area.
2. Tree Map for "Staged for deletion" area: Quick way to remove files from the commit tree.
3. File INDEX to contain all the serialized "objects" for adding or removal

### Blob

#### Fields
1. The parent file's filename
2. The blob's own hashcode?
3. The parent's file content

### Commit

#### Fields

1. String message: Commit's message
2. Date timestamp: The time when the commmit was creaged.
3. String parentCommit: The SHA1 from the parent commit.
4. String secondParentReference: The SHA1 from the second parent commit.
5. TreeMap for Blobs: A way to store which blobs are pointed by the current commit.

## Algorithms
1. java gitlet.Main init: Creates a folder for blobs, and a folder for commits
2. java gitlet.Main add .... : This command will add the following file(s) into the "Staged for addition" TreeMap, while simultaneously creating a blob for said file(s), and creating a key->value pair with the filename of the file and it's correspoding blob inside the TreeMap(filename->blob) Creates an "index" text file. The "index" text file will have all the key->value pairs inside the "Staged for addition" TreeMap.
3. java gitlet.Main commit ... : This command will clone(will figure out how to do this later) the current HEAD commit, and insert the correct MetaData(message, timestamp), save the parent Commit's SHA1 inside an instance variable, and adding the "filename(s)" of the files commited inside it's internal TreeMap. Therefore creating a key->value pair between filenames and their respective blob's SHA1 (filename->blob's SHA1);
4. the SHA-1 hash value, rendered as a 40-character hexadecimal string, makes a convenient file name for storing your data in your .gitlet directory. It also gives you a convenient way to compare two files (blobs) to see if they have the same contents: if their SHA-1s are the same, we simply assume the files are the same.
5. As you can see, each commit (rectangle) points to some blobs (circles), which contain file contents. The commits contain the file names and references to these blobs, as well as a parent link. These references, depicted as arrows, are represented in the .gitlet directory using their SHA-1 hash values.
6. java gitlet.Main java gitlet.Main checkout -- [file name] : We use the HEAD commit to basically rewrite the [file name] in which ever way it as committed  in the HEAD commit and if we don't have any [file name] in our working directory, does that mean that we "create" that file in our working directory using the information on our HEAD commit.

## Persistence
1. For the staging area: An "index" text file is created after the first "gitlet add". The "index" text file will have the "stage class" serialized/saved inside it.
2. Blobs will be stored inside a directory. Each blob will be linked their respective filename at the moment they are "added" inside the staging area.
3. Commits will be stored inside a directory. Each commit will have the SHA1 of their respective blobs inside a treemap.

