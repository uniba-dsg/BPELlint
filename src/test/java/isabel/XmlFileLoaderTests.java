package isabel;

import isabel.tool.ValidationException;
import isabel.tool.imports.ProcessContainerLoader;
import org.junit.Before;
import org.junit.Test;

public class XmlFileLoaderTests {

	private ProcessContainerLoader fileHandler;

	@Before
	public void setUp() throws Exception {
		fileHandler = new ProcessContainerLoader();
	}

	@Test(expected = NullPointerException.class)
	public void whenCalledLoadAllProcessFilesWithNull_shouldThrowIllegalArgumentException()
			throws Exception {
		fileHandler.load(null);
	}

	@Test(expected = ValidationException.class)
	public void whenCalledLoadAllProcessFilesWithNonExistentFile_shouldThrowIllegalArgumentException()
			throws Exception {
		fileHandler.load("nowhere/nonexistent/file");
	}
}
