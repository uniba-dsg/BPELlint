package de.uniba.wiai.dsg.ss12.isabel.tool.schemaValidation.io;

import java.io.File;
import java.io.FilenameFilter;

class DirFilenameFilter implements FilenameFilter {

	@Override
	public boolean accept(File dir, String dirName) {
		return new File(dir, dirName).isDirectory();
	}

}
