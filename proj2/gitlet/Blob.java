package gitlet;

import java.io.File;
import java.io.Serializable;

import static gitlet.Utils.join;
import static gitlet.Utils.sha1;

public class Blob implements Serializable {

    // The blobs directory
    public static final File BLOB_DIR = join(Repository.GITLET_DIR, "blobs");

    // The parent file's filename
    private String parentFileName;

    // The blob's own hashcode?
    private String hashCode;

    // The parent's file content
    private String content;

    public Blob(String fileName) {
        parentFileName = fileName;

        content = Utils.readContentsAsString(join(Repository.CWD, fileName));

        hashCode = sha1(content);
    }

    public String getParentFileName() {
        return parentFileName;
    }

    public String getHashCode() {
        return hashCode;
    }

     public String getContent() {
        return content;
    }
}
