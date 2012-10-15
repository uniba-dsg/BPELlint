package de.uniba.wiai.dsg.ss12.isabel.test;

import org.junit.Before;
import org.junit.Test;

import de.uniba.wiai.dsg.ss12.isabel.tool.ValidationException;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.XmlFileLoader;

public class XmlFileLoaderTests {

	private XmlFileLoader fileHandler;

	@Before
	public void setUp() throws Exception {
		fileHandler = new XmlFileLoader();
	}

	@Test(expected = ValidationException.class)
	public void whenCalledLoadAllProcessFilesWithNull_shouldThrowIllegalArgumentException()
			throws Exception {
		fileHandler.loadAllProcessFiles(null);
	}

	@Test(expected = ValidationException.class)
	public void whenCalledLoadAllProcessFilesWithNonExsistentFile_shouldThrowIllegalArgumentException()
			throws Exception {
		fileHandler.loadAllProcessFiles("nowhere/nonexsistent/file");
	}
}
