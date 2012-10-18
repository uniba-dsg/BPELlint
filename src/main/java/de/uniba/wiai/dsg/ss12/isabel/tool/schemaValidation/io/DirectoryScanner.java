package de.uniba.wiai.dsg.ss12.isabel.tool.schemaValidation.io;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DirectoryScanner {

	private final File dir;
	private final String fileEnding;
	private List<File> files = new ArrayList<>();

	public DirectoryScanner(File dir, String fileEnding) {
		this.dir = dir;
		this.fileEnding = fileEnding;
	}

	public List<File> scan() {
		files = new ArrayList<>();

		scanDir(dir);
		return files;
	}

	private void scanDir(File dir) {
		// search folder
		for (File file : dir.listFiles(new FileFilenameFilter(fileEnding))) {
			this.files.add(file);
		}

		// search file
		for (File file : dir.listFiles(new DirFilenameFilter())) {
			scanDir(file);
		}
	}

}