package de.uniba.wiai.dsg.ss12.isabel.test;

import de.uniba.wiai.dsg.ss12.isabel.tool.imports.DocumentEntry;
import nu.xom.Document;
import nu.xom.Element;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DocumentEntryTests {

	private DocumentEntry entry;
	private Document document;

	@Before
	public void setUp() throws Exception {
		document = new Document(new Element("a"));
		entry = new DocumentEntry("a", "a", document);
	}

	@Test(expected = IllegalArgumentException.class)
	public void whenConstructedWithStringStringNull_defaultConstructorShouldThrowIllegalArgumentException()
			throws Exception {
		new DocumentEntry("a", "a", null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void whenConstructedWithStringNullDocument_defaultConstructorShouldThrowIllegalArgumentException()
			throws Exception {
		new DocumentEntry("a", null, new Document(new Element("a")));
	}

	@Test(expected = IllegalArgumentException.class)
	public void whenConstructedWithNullStringDocument_defaultConstructorShouldThrowIllegalArgumentException()
			throws Exception {
		new DocumentEntry(null, "a", new Document(new Element("a")));
	}

	@Test
	public void givenPathA_whenConstructedPathStringDocument_getFilePathShouldReturnA()
			throws Exception {
		String filePath = entry.getFilePath();

		assertEquals("a", filePath);
	}

	@Test
	public void givenTargetNamespaceA_whenConstructedWithGiven_getTargetNamespaceShouldReturnA()
			throws Exception {
		String qName = entry.getTargetNamespace();

		assertEquals("a", qName);
	}

	@Test
	public void givenDocumentElementA_whenConstructedStringStringDocument_getQNameShouldReturnA()
			throws Exception {
		Document document = entry.getDocument();

		assertEquals(this.document, document);
	}
}
