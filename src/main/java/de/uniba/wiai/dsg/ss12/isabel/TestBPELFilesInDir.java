package de.uniba.wiai.dsg.ss12.isabel;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestBPELFilesInDir {

	public static void main(String[] args) throws IOException {
		// validation
		if(args == null || args.length != 1 || args[0] == null || args[0].isEmpty()){
			throw new IllegalArgumentException("Usage: PATH");
		}

		// file tree iteration
		Path startingDirectory = Paths.get(args[0]);
		try(DirectoryStream<Path> paths = Files.newDirectoryStream(startingDirectory)){
			for(Path path : paths) {
				if(Files.isRegularFile(path) && path.toString().endsWith(".bpel")){
					// start testing
					IsabelTool.main(new String[]{path.toString()});
				} else if(Files.isDirectory(path)){
					// recursion
					TestBPELFilesInDir.main(new String[]{path.toString()});
				}
			}
		}
	}

}
