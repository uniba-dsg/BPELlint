package isabel;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

class BetsyTests {

	private List<Object[]> betsyTests;

	public BetsyTests() throws IOException {
		Path baseDir = Paths.get("Testcases/betsy");
		betsyTests = new LinkedList<>();
		
		try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(baseDir)) {
			for (Path dir : dirStream) {
				if (!Files.isDirectory(dir)) {
					continue;
				}
				agregateBpelFiles(dir);
			}
		}
		
		betsyTests = Collections.unmodifiableList(betsyTests);
	}

	private void agregateBpelFiles(Path dir) throws IOException {
		try (DirectoryStream<Path> fileStream = Files.newDirectoryStream(dir, "*.bpel")) {
			for (Path path : fileStream) {
				betsyTests.add(new Object[] { path.toString(), "" });
			}
		}
	}

	
	List<Object[]> list() {
		return betsyTests;
	}
}
