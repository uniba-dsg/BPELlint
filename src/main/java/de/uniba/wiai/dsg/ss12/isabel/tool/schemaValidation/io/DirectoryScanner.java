package de.uniba.wiai.dsg.ss12.isabel.tool.schemaValidation.io;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class DirectoryScanner {

    private final String fileEnding;

    public DirectoryScanner(String fileEnding) {
        this.fileEnding = fileEnding;
    }

    public List<File> scanDir(File dir) {
        List<File> result = new LinkedList<>();

        // add all corresponding files within the current folder
        Collections.addAll(result, dir.listFiles(new FileFilenameFilter(fileEnding)));

        // search for subdirectories
        for (File file : dir.listFiles(new DirFilenameFilter())) {
            result.addAll(scanDir(file));
        }

        return result;
    }

}