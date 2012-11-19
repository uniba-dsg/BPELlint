package de.uniba.wiai.dsg.ss12.isabel.tool.imports;

import de.uniba.wiai.dsg.ss12.isabel.tool.ValidationException;
import de.uniba.wiai.dsg.ss12.isabel.tool.helper.NodeHelper;
import de.uniba.wiai.dsg.ss12.isabel.tool.impl.Standards;
import nu.xom.*;
import org.pmw.tinylog.Logger;

import java.io.*;
import java.net.URI;
import java.nio.file.Paths;

import static de.uniba.wiai.dsg.ss12.isabel.tool.impl.Standards.CONTEXT;

public class XmlFileLoader {

	public static final String XMLSCHEMA_XSD = "/xsd/XMLSchema.xsd";

	private final Builder builder = new Builder(new LocationAwareNodeFactory());
	private BpelProcessFiles result;

	public BpelProcessFiles loadAllProcessFiles(String bpelFilePath)
			throws ValidationException {

		if (bpelFilePath == null) {
			throw new ValidationException(new IllegalArgumentException(
					"parameter bpelFilePath must not be null"));
		}

		try {
			return loadAllProcessFilesWithoutExceptions(bpelFilePath);
		} catch (ValidityException e) {
			throw new ValidationException(
					"Loading failed: Not a valid BPEL-File", e);
		} catch (ParsingException e) {
			throw new ValidationException("Loading failed: Not Parsable", e);
		} catch (FileNotFoundException e) {
			throw new ValidationException(
					"Loading failed: File was not found", e);
		} catch (IOException e) {
			throw new ValidationException(
					"Loading failed: File-Reading Error", e);
		}

	}

	private BpelProcessFiles loadAllProcessFilesWithoutExceptions(String bpelFilePath) throws ParsingException, IOException, ValidationException {
		loadBpelFile(bpelFilePath);
		loadXmlSchema();
		loadBpelImports();

		return result.createImmutable();
	}

	private void loadBpelImports() throws ParsingException, IOException {
		for (Node importNode : result.getImports()) {
			loadWsdlOrXsd(importNode);
		}
	}

	private void loadWsdlOrXsd(Node importNode) throws ParsingException, IOException {
		Logger.debug("Loading <bpel:import> reference " + importNode.toXML());
		DocumentEntry entry = createImportDocumentEntry(importNode);
		if (entry.isWsdl()) {
			loadWSDL(entry);
		} else if (entry.isXsd()) {
			loadXSD(entry);
		} else {
			throw new IllegalStateException("Bad <import> found " + importNode.toXML());
		}
	}

	private void loadXmlSchema() throws ValidationException, ParsingException, IOException {
		try (InputStream stream = XmlFileLoader.class.getResourceAsStream(XMLSCHEMA_XSD)) {
			Document xmlSchemaDom = builder.build(stream);
			Logger.debug("XML Schema Resource URL: " + xmlSchemaDom.getBaseURI());
			DocumentEntry xmlSchemaEntry = new DocumentEntry(xmlSchemaDom);

			result.addXsd(xmlSchemaEntry);
		}
	}

	private DocumentEntry loadBpelFile(String bpelFilePath) throws ParsingException, IOException, ValidationException {
		try {
			Document bpelDom = builder.build(new File(bpelFilePath));
			DocumentEntry bpel = new DocumentEntry(bpelDom);

			// create and set result
			result = new BpelProcessFiles();
			result.setBpel(bpel);

			return bpel;
		} catch (Exception e) {
			throw new ValidationException("Error with file " + bpelFilePath, e);
		}
	}

	private void loadWSDL(DocumentEntry wsdlEntry) throws ParsingException, IOException {
		if (!result.getAllWsdls().contains(wsdlEntry)) {
			result.addWsdl(wsdlEntry);

			for (Node importNode : wsdlEntry.getDocument().query("//wsdl:import", CONTEXT)) {
				loadWsdlOrXsd(importNode);
			}

			addWsdlXsd(wsdlEntry);
		}
	}

	private void loadXSD(DocumentEntry entry) throws ParsingException, IOException {
		if (!result.getAllXsds().contains(entry)) {
			result.addXsd(entry);
			// TODO check if this is the right xsd file
			for (Node importNode : entry.getDocument().query("//xsd:import", CONTEXT)) {
				loadWsdlOrXsd(importNode);
			}
		}
	}

	private void addWsdlXsd(DocumentEntry entry) throws
			ParsingException, IOException {
		Nodes typesNodes = entry.getDocument().query("//wsdl:types/*", CONTEXT);

		if (typesNodes.size() == 0) {
			return;
		}

		Node schemaNode = typesNodes.get(0);
		if (isXsdNode(schemaNode)
				&& new NodeHelper(schemaNode).hasLocalName("schema")) {
			result.addSchema(schemaNode);
			addXsdImports(schemaNode);
		}
	}

	private void addXsdImports(Node schemaNode) throws
			ParsingException, IOException {
		DocumentEntry xsdEntry;
		Nodes schemaChildren = schemaNode.query("child::*", CONTEXT);
		for (Node node : schemaChildren) {
			if (isXsdNode(node)
					&& new NodeHelper(schemaNode).hasLocalName("import")) {
				xsdEntry = createImportDocumentEntry(node);
				result.addXsd(xsdEntry);
			}
		}
	}

	private String getNodeDirectory(Node node) {
		if (node.getBaseURI().isEmpty()) {
			return null;
		}

		return Paths.get(URI.create(node.getBaseURI())).getParent().toString();
	}

	private boolean isXsdNode(Node node) {
		return ((Element) node).getNamespaceURI().equals(
				Standards.XSD_NAMESPACE);
	}

	private DocumentEntry createImportDocumentEntry(Node importNode)
			throws ParsingException, IOException {
		Logger.debug("Creating entry for " + importNode.toXML());

		String locationPath = Paths.get(getNodeDirectory(importNode),
				getImportPath(importNode)).toString();
		// remove relative path elements like .. and .
		File importFile = Paths.get(locationPath).toFile().getCanonicalFile();
		Document importFileDom = builder.build(importFile);

		DocumentEntry entry = new DocumentEntry(importFileDom);
		Logger.debug("Loaded " + importFile + " (" + importNode.toXML() + ") as " + entry);

		return entry;
	}

	private String getImportPath(Node node) {
		NodeHelper nodeHelper = new NodeHelper(node);

		if (nodeHelper.hasAttribute("schemaLocation")) {
			return Paths.get(nodeHelper.getAttribute("schemaLocation")).toString();
		} else if (nodeHelper.hasAttribute("location")) {
			return Paths.get(nodeHelper.getAttribute("location")).toString();
		}

		return null;
	}
}