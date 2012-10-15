package de.uniba.wiai.dsg.ss12.isabel.tool.schemaValidation.io;

import java.io.File;
import java.io.FilenameFilter;

public class FileFilenameFilter implements FilenameFilter {

	private String fileEnding;

	public FileFilenameFilter(String fileEnding) {
		this.fileEnding = fileEnding.toLowerCase();
	}

	@Override
	public boolean accept(File dir, String fileName) {
		return new File(dir, fileName).isFile()
				&& fileName.toLowerCase().endsWith(fileEnding);
	}

}
