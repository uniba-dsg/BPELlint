package isabel.tool.imports;

import isabel.tool.helper.NodesUtil;
import isabel.tool.impl.NavigationException;
import isabel.tool.impl.Standards;
import nu.xom.Document;
import nu.xom.Node;
import nu.xom.Nodes;
import org.pmw.tinylog.Logger;

import java.io.File;
import java.util.*;

import static isabel.tool.impl.Standards.CONTEXT;
import static isabel.tool.impl.Standards.XSD_NAMESPACE;

public class ProcessContainer {

	private XmlFile bpel;
	private List<XmlFile> wsdlList = new ArrayList<>();
	private List<XmlFile> xsdList = new ArrayList<>();
	private List<Node> xsdSchemaList = new ArrayList<>();

	public void addWsdl(XmlFile xmlFile) {
		Objects.requireNonNull(xmlFile, "a xmlFile reference is required");
		xmlFile.failUnlessWsdl();
		wsdlList.add(xmlFile);
	}

	public void addXsd(XmlFile xmlFile) {
		Objects.requireNonNull(xmlFile, "a xmlFile reference is required");
		xmlFile.failUnlessXsd();
		xsdList.add(xmlFile);
	}

	public void addSchema(Node schema) {
		Objects.requireNonNull(schema, "a schema reference is required");
		xsdSchemaList.add(Objects.requireNonNull(schema,
				"a schema reference is required"));
	}

	public void setBpel(XmlFile xmlFile) {
		Objects.requireNonNull(xmlFile, "a bpel reference is required");
		xmlFile.failUnlessBpel();
		this.bpel = xmlFile;
	}

	public String getAbsoluteBpelFolder() {
		return new File(this.bpel.getFilePath()).getParent();
	}

	public XmlFile getBpel() {
		return bpel;
	}

	public List<XmlFile> getAllWsdls() {
		return new ArrayList<>(wsdlList);
	}

	public List<XmlFile> getAllXsds() {
		return new ArrayList<>(xsdList);
	}

	public List<Node> getXsdSchema() {
		List<Node> xsdSchema = new ArrayList<>(xsdSchemaList);
		for (XmlFile xsdNode : getAllXsds())
			xsdSchema.add(xsdNode.getDocument().getChild(0));

		return xsdSchema;
	}

	public Document getXmlSchema() throws NavigationException {
		for (XmlFile xmlFile : getAllXsds())
			if (XSD_NAMESPACE.equals(xmlFile.getTargetNamespace()))
				return xmlFile.getDocument();

		throw new NavigationException(
				"XMLSchema should have been imported, but haven't.");
	}

	public Document getWsdlByTargetNamespace(String searchedTargetNamespace)
			throws NavigationException {
		for (XmlFile wsdlEntry : getAllWsdls())
			if (wsdlEntry.getTargetNamespace().equals(searchedTargetNamespace))
				return wsdlEntry.getDocument();

		throw new NavigationException("Document does not exist");
	}

	public List<Node> getAllPropertyAliases() {
		List<Node> propertyAliases = new LinkedList<>();
		for (XmlFile xmlFile : getAllWsdls()) {
			propertyAliases.addAll(NodesUtil.toList(xmlFile.getDocument()
					.query("//vprop:propertyAlias", CONTEXT)));
		}
		return propertyAliases;
	}

	public List<Node> getAllProperties() {
		List<Node> propertyAliases = new LinkedList<>();
		for (XmlFile xmlFile : getAllWsdls()) {
			propertyAliases.addAll(NodesUtil.toList(xmlFile.getDocument()
					.query("//vprop:property", CONTEXT)));
		}
		return propertyAliases;
	}

	public Nodes getCorrelationSets() {
		return getBpel().getDocument().query("//bpel:correlationSet", CONTEXT);
	}

	public ProcessContainer createImmutable() {
		Logger.info("Creating immutable ProcessContainer: " + this);

		validate();

		ProcessContainer processContainer = new ProcessContainer();
		processContainer.setBpel(bpel);
		processContainer.wsdlList = Collections.unmodifiableList(wsdlList);
		processContainer.xsdList = Collections.unmodifiableList(xsdList);
		processContainer.xsdSchemaList = Collections
				.unmodifiableList(xsdSchemaList);

		return processContainer;
	}

	public Nodes getImports() {
		return getBpel().getDocument().query("//bpel:import", CONTEXT);
	}

	void validate() {
		// assertion
		if (getAllWsdls().isEmpty()) {
			throw new IllegalStateException(
					"At least one WSDL file is required");
		}
	}

	public String toString() {
		return new ProcessContainerPrinter(this).toString();
	}

}
