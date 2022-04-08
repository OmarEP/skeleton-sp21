package gitlet;

import java.io.File;
import java.io.Serializable;

import static gitlet.Utils.join;
import static gitlet.Utils.sha1;

public class Blob implements Serializable {

    // The blobs directory
    public static final File BLOB_DIR = join(Repository.GITLET_DIR, "blobs");

    // The parent file's filename
    private String parentFile;

    // The blob's own hashcode?
     private String hashCode;

    // The parent's file content
    private String content;

    public Blob(String fileName) {
        parentFile = fileName;

        content = Utils.readContentsAsString(join(Repository.CWD, fileName));

        hashCode = sha1(content);
    }
}
