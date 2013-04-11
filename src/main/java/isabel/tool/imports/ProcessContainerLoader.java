package isabel.tool.imports;

import isabel.tool.ValidationException;
import isabel.tool.helper.NodeHelper;
import isabel.tool.impl.Standards;
import nu.xom.*;

import java.io.FileNotFoundException;
import java.io.IOException;

import static isabel.tool.impl.Standards.CONTEXT;

public class ProcessContainerLoader {

	public static final String XMLSCHEMA_XSD = "/xsd/XMLSchema.xsd";
	private final XmlFileLoader xmlFileLoader = new XmlFileLoader();

	private ProcessContainer result;

	public ProcessContainer load(String bpelFilePath) throws ValidationException {
		try {
			return loadAllProcessFilesWithoutExceptions(bpelFilePath);
		} catch (ValidityException e) {
			throw new ValidationException("Loading failed: Not a valid BPEL-File", e);
		} catch (ParsingException e) {
			throw new ValidationException("Loading failed: Not Parsable", e);
		} catch (FileNotFoundException e) {
			throw new ValidationException("Loading failed: File was not found", e);
		} catch (IOException e) {
			throw new ValidationException("Loading failed: File-Reading Error", e);
		}
	}

	private ProcessContainer loadAllProcessFilesWithoutExceptions(String bpelFilePath)
			throws ParsingException, IOException, ValidationException {
		result = new ProcessContainer();

		result.setBpel(xmlFileLoader.load(bpelFilePath));
		result.add(xmlFileLoader.loadFromResourceStream(XMLSCHEMA_XSD));
		loadBpelImports();

		return result.validateAndFinalize();
	}

	private void loadBpelImports() throws ParsingException, IOException {
		for (Node importNode : result.getImports()) {
			XmlFile entry = xmlFileLoader.loadImportNode(importNode);
			this.result.addDirectlyImported(entry);
			loadImports(entry);
		}
	}

	private void loadImports(XmlFile entry) throws ParsingException, IOException {
		if (entry.isWsdl()) {
			loadWsdlImports(entry);
			loadXsdImportsInWsdl(entry);
		} else if (entry.isXsd()) {
			loadXsdImports(entry);
		}
	}

	private void loadXsdImports(XmlFile entry) throws ParsingException, IOException {
		for (Node importNode : entry.getDocument().query("//xsd:import", CONTEXT)) {
			XmlFile xmlFile = xmlFileLoader.loadImportNode(importNode);
			if (!result.contains(xmlFile)) {
				result.add(xmlFile);
				loadImports(xmlFile);
			}
		}
	}

	private void loadXsdImportsInWsdl(XmlFile entry) throws ParsingException, IOException {
		Nodes typesNodes = entry.getDocument().query("//wsdl:types/*", CONTEXT);

		if (typesNodes.isEmpty()) {
			return;
		}

		Node schemaNode = typesNodes.get(0);
		if (isXsdNode(schemaNode) && new NodeHelper(schemaNode).hasLocalName("schema")) {
			result.addSchema(schemaNode);
			addXsdImports(schemaNode);
		}
	}

	private void loadWsdlImports(XmlFile entry) throws ParsingException, IOException {
		for (Node importNode : entry.getDocument().query("//wsdl:import", CONTEXT)) {
			XmlFile xmlFile = xmlFileLoader.loadImportNode(importNode);
			if (!result.contains(xmlFile)) {
				result.add(xmlFile);
				loadImports(xmlFile);
			}
		}
	}

	private void addXsdImports(Node schemaNode) throws ParsingException, IOException {
		Nodes schemaChildren = schemaNode.query("child::*", CONTEXT);
		for (Node node : schemaChildren) {
			if (isXsdNode(node) && new NodeHelper(schemaNode).hasLocalName("import")) {
				result.add(xmlFileLoader.loadImportNode(node));
			}
		}
	}

	private boolean isXsdNode(Node node) {
		return ((Element) node).getNamespaceURI().equals(Standards.XSD_NAMESPACE);
	}


}