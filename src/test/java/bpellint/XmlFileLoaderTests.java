package bpellint;


import org.junit.Before;
import org.junit.Test;

import bpellint.tool.imports.ImportException;
import bpellint.tool.imports.ProcessContainerLoader;

import java.nio.file.Paths;

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

    @Test(expected = ImportException.class)
    public void whenCalledLoadAllProcessFilesWithNonExistentFile_shouldThrowIllegalArgumentException()
            throws Exception {
        fileHandler.load(Paths.get("nowhere/nonexistent/file"));
    }
}
