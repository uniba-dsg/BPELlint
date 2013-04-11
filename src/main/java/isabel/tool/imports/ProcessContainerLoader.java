package isabel.tool.imports;

import isabel.tool.ValidationException;
import isabel.tool.helper.NodeHelper;
import isabel.tool.impl.Standards;
import nu.xom.*;
import org.pmw.tinylog.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;

import static isabel.tool.impl.Standards.CONTEXT;

public class ProcessContainerLoader {

	public static final String XMLSCHEMA_XSD = "/xsd/XMLSchema.xsd";
	private final XmlFileLoader xmlFileLoader = new XmlFileLoader();

	private ProcessContainer result;

	public ProcessContainer load(String bpelFilePath)
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
			throw new ValidationException("Loading failed: File was not found", e);
		} catch (IOException e) {
			throw new ValidationException("Loading failed: File-Reading Error", e);
		}

	}

	private ProcessContainer loadAllProcessFilesWithoutExceptions(
			String bpelFilePath) throws ParsingException, IOException,
			ValidationException {
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

	private void loadWsdlOrXsd(Node importNode) throws ParsingException,
			IOException {
		Logger.debug("Loading <bpel:import> reference " + importNode.toXML());
		XmlFile entry = xmlFileLoader.loadImportNode(importNode);
		if (entry.isWsdl()) {
			loadWSDL(entry);
		} else if (entry.isXsd()) {
			loadXSD(entry);
		} else {
			throw new IllegalStateException("Bad <import> found "
					+ importNode.toXML());
		}
	}

	private void loadXmlSchema() throws ValidationException, ParsingException,
			IOException {
		result.addXsd(xmlFileLoader.loadFromResourceStream(XMLSCHEMA_XSD));
	}

	private XmlFile loadBpelFile(String bpelFilePath)
			throws ParsingException, IOException, ValidationException {
		try {
			XmlFile bpel = xmlFileLoader.load(bpelFilePath);

			// create and set result
			result = new ProcessContainer();
			result.setBpel(bpel);

			return bpel;
		} catch (Exception e) {
			throw new ValidationException("Error with file " + bpelFilePath, e);
		}
	}

	private void loadWSDL(XmlFile wsdlEntry) throws ParsingException,
			IOException {
		if (!result.getAllWsdls().contains(wsdlEntry)) {
			result.addWsdl(wsdlEntry);

			for (Node importNode : wsdlEntry.getDocument().query("//wsdl:import", CONTEXT)) {
				loadWsdlOrXsd(importNode);
			}

			addWsdlXsd(wsdlEntry);
		}
	}

	private void loadXSD(XmlFile entry) throws ParsingException,
			IOException {
		if (!result.getAllXsds().contains(entry)) {
			result.addXsd(entry);
			// TODO check if this is the right xsd file
			for (Node importNode : entry.getDocument().query("//xsd:import", CONTEXT)) {
				loadWsdlOrXsd(importNode);
			}
		}
	}

	private void addWsdlXsd(XmlFile entry) throws ParsingException,
			IOException {
		Nodes typesNodes = entry.getDocument().query("//wsdl:types/*", CONTEXT);

		if (typesNodes.size() == 0) {
			return;
		}

		Node schemaNode = typesNodes.get(0);
		if (isXsdNode(schemaNode) && new NodeHelper(schemaNode).hasLocalName("schema")) {
			result.addSchema(schemaNode);
			addXsdImports(schemaNode);
		}
	}

	private void addXsdImports(Node schemaNode) throws ParsingException,
			IOException {
		Nodes schemaChildren = schemaNode.query("child::*", CONTEXT);
		for (Node node : schemaChildren) {
			if (isXsdNode(node) && new NodeHelper(schemaNode).hasLocalName("import")) {
				result.addXsd(xmlFileLoader.loadImportNode(node));
			}
		}
	}

	private boolean isXsdNode(Node node) {
		return ((Element) node).getNamespaceURI().equals(Standards.XSD_NAMESPACE);
	}


}