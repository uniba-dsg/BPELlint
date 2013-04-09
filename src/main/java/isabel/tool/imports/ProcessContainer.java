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

	public ProcessContainer() {
	}

	public void addWsdl(XmlFile wsdl) {
		// check not null
		Objects.requireNonNull(wsdl, "a wsdl reference is required");

		// check namespace
		String namespaceURI = wsdl.getDocument().getRootElement()
				.getNamespaceURI();
		if (!namespaceURI.equals(Standards.WSDL_NAMESPACE)) {
			throw new IllegalArgumentException(
					"expected WSDL namespace for WSDL file but got "
							+ namespaceURI + " in file " + wsdl.getFilePath());
		}

		// add
		wsdlList.add(wsdl);
	}

	public void addXsd(XmlFile xsd) {
		// check not null
		Objects.requireNonNull(xsd, "a xsd reference is required");

		// check namespace
		String namespaceURI = xsd.getDocument().getRootElement()
				.getNamespaceURI();
		if (!namespaceURI.equals(Standards.XSD_NAMESPACE)) {
			throw new IllegalArgumentException(
					"expected XSD namespace for XSD file but got "
							+ namespaceURI + " in file " + xsd.getFilePath());
		}

		// add
		xsdList.add(xsd);
	}

	public void addSchema(Node schema) {
		xsdSchemaList.add(Objects.requireNonNull(schema,
				"a schema reference is required"));
	}

	public void setBpel(XmlFile bpel) {
		// check not null
		Objects.requireNonNull(bpel, "a bpel reference is required");

		// check namespace
		String namespaceURI = bpel.getDocument().getRootElement()
				.getNamespaceURI();
		if (!namespaceURI.equals(Standards.BPEL_NAMESPACE)) {
			throw new IllegalArgumentException(
					"expected BPEL namespace for BPEL file but got "
							+ namespaceURI + " in file " + bpel.getFilePath());
		}

		this.bpel = bpel;
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
