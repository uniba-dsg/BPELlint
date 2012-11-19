package de.uniba.wiai.dsg.ss12.isabel.tool.imports;

import de.uniba.wiai.dsg.ss12.isabel.tool.helper.NodesUtil;
import de.uniba.wiai.dsg.ss12.isabel.tool.impl.NavigationException;
import de.uniba.wiai.dsg.ss12.isabel.tool.impl.Standards;
import nu.xom.Document;
import nu.xom.Node;
import nu.xom.Nodes;
import org.pmw.tinylog.Logger;

import java.io.File;
import java.util.*;

import static de.uniba.wiai.dsg.ss12.isabel.tool.impl.Standards.CONTEXT;
import static de.uniba.wiai.dsg.ss12.isabel.tool.impl.Standards.XSD_NAMESPACE;

public class BpelProcessFiles {

	private DocumentEntry bpel;
	private List<DocumentEntry> wsdlList = new ArrayList<>();
	private List<DocumentEntry> xsdList = new ArrayList<>();
	private List<Node> xsdSchemaList = new ArrayList<>();

	public BpelProcessFiles() {
	}

	public void addWsdl(DocumentEntry wsdl) {
		// check not null
		Objects.requireNonNull(wsdl, "a wsdl reference is required");

		// check namespace
		String namespaceURI = wsdl.getDocument().getRootElement().getNamespaceURI();
		if(!namespaceURI.equals(Standards.WSDL_NAMESPACE)){
			throw new IllegalArgumentException("expected WSDL namespace for WSDL file but got " + namespaceURI + " in file " + wsdl.getFilePath());
		}

		// add
		wsdlList.add(wsdl);
	}

	public void addXsd(DocumentEntry xsd) {
		// check not null
		Objects.requireNonNull(xsd, "a xsd reference is required");

		// check namespace
		String namespaceURI = xsd.getDocument().getRootElement().getNamespaceURI();
		if(!namespaceURI.equals(Standards.XSD_NAMESPACE)){
			throw new IllegalArgumentException("expected XSD namespace for XSD file but got " + namespaceURI + " in file " + xsd.getFilePath());
		}

		// add
		xsdList.add(xsd);
	}

	public void addSchema(Node schema) {
		xsdSchemaList.add(Objects.requireNonNull(schema, "a schema reference is required"));
	}

	public void setBpel(DocumentEntry bpel) {
		// check not null
		Objects.requireNonNull(bpel, "a bpel reference is required");

		// check namespace
		String namespaceURI = bpel.getDocument().getRootElement().getNamespaceURI();
		if(!namespaceURI.equals(Standards.BPEL_NAMESPACE)){
			throw new IllegalArgumentException("expected BPEL namespace for BPEL file but got " + namespaceURI + " in file " + bpel.getFilePath());
		}

		this.bpel = bpel;
	}

	public String getAbsoluteBpelFolder() {
		return new File(this.bpel.getFilePath()).getParent();
	}

	public DocumentEntry getBpel() {
		return bpel;
	}

	public List<DocumentEntry> getAllWsdls() {
		return new ArrayList<>(wsdlList);
	}

	public List<DocumentEntry> getAllXsds() {
		return new ArrayList<>(xsdList);
	}

	public List<Node> getXsdSchema() {
		List<Node> xsdSchema = new ArrayList<>(xsdSchemaList);
		for (DocumentEntry xsdNode : getAllXsds())
			xsdSchema.add(xsdNode.getDocument().getChild(0));

		return xsdSchema;
	}

	public Document getXmlSchema() throws NavigationException {
		for (DocumentEntry documentEntry : getAllXsds())
			if (XSD_NAMESPACE.equals(documentEntry.getTargetNamespace()))
				return documentEntry.getDocument();

		throw new NavigationException(
				"XMLSchema should have been imported, but haven't.");
	}

	public Document getWsdlByTargetNamespace(
			String searchedTargetNamespace) throws NavigationException {
		for (DocumentEntry wsdlEntry : getAllWsdls())
			if (wsdlEntry.getTargetNamespace().equals(searchedTargetNamespace))
				return wsdlEntry.getDocument();

		throw new NavigationException("Document does not exist");
	}

	public List<Node> getAllPropertyAliases() {
		List<Node> propertyAliases = new LinkedList<>();
		for (DocumentEntry documentEntry : getAllWsdls()) {
			propertyAliases.addAll(NodesUtil.toList(documentEntry.getDocument().query(
					"//vprop:propertyAlias", CONTEXT)));
		}
		return propertyAliases;
	}

	public List<Node> getAllProperties() {
		List<Node> propertyAliases = new LinkedList<>();
		for (DocumentEntry documentEntry : getAllWsdls()) {
			propertyAliases.addAll(NodesUtil.toList(documentEntry.getDocument().query(
					"//vprop:property", CONTEXT)));
		}
		return propertyAliases;
	}

	public Nodes getCorrelationSets() {
		return getBpel().getDocument()
				.query("//bpel:correlationSet", CONTEXT);
	}

	public BpelProcessFiles createImmutable() {
		Logger.debug("Creating immutable BpelProcessFiles: " + getStatus());
		validate();

		BpelProcessFiles bpelProcessFiles = new BpelProcessFiles();
		bpelProcessFiles.setBpel(bpel);
		bpelProcessFiles.wsdlList = Collections.unmodifiableList(wsdlList);
		bpelProcessFiles.xsdList = Collections.unmodifiableList(xsdList);
		bpelProcessFiles.xsdSchemaList = Collections.unmodifiableList(xsdSchemaList);

		return bpelProcessFiles;
	}

	public String getStatus() {
		String result = "";
		if(bpel != null){
			result += "1";
		} else {
			result += "0";
		}
		result += " bpel";
		result += " " + wsdlList.size() + " wsdl(s)";
		result += " " + xsdList.size() + " xsd(s)";
		result += " " + xsdSchemaList.size() + " schema(s)";

		return result;
	}

	public Nodes getImports() {
		return getBpel().getDocument().query("//bpel:import", CONTEXT);
	}

	void validate() {
		// assertion
		if (getAllWsdls().isEmpty()) {
			throw new IllegalStateException("At least one WSDL file is required");
		}
	}
}
