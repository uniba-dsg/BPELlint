package de.uniba.wiai.dsg.ss12.isabel.tool.imports;

import nu.xom.Node;

import java.util.ArrayList;
import java.util.List;

public class BpelProcessFiles {

	private final DocumentEntry bpel;
	private final List<DocumentEntry> wsdlList;
	private final List<DocumentEntry> xsdList;
	private final List<Node> xsdSchemaList;
	private final String absoluteBpelFilePath;

	public BpelProcessFiles(DocumentEntry bpel, List<DocumentEntry> wsdlList,
	                        List<DocumentEntry> xsdList, List<Node> xsdSchemaList,
	                        String absoluteBpelFilePath) {

		if (bpel == null) {
			throw new IllegalStateException("The BPEL DOM must be initialized "
					+ "before calling this method.");
		}

		this.bpel = bpel;
		this.wsdlList = wsdlList;
		this.xsdList = xsdList;
		this.xsdSchemaList = xsdSchemaList;
		this.absoluteBpelFilePath = absoluteBpelFilePath;
	}

	public String getAbsoluteBpelFilePath() {
		return absoluteBpelFilePath;
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
		for (DocumentEntry xsdNode : getAllXsds()) {
			xsdSchema.add(xsdNode.getDocument().getChild(0));
		}
		return xsdSchema;
	}
}
