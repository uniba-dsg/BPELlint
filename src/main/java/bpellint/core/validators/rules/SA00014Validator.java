package bpellint.core.validators.rules;

import api.SimpleValidationResult;
import bpellint.core.model.NodeHelper;
import bpellint.core.model.ProcessContainer;
import bpellint.core.model.Standards;
import bpellint.core.model.XmlFile;
import nu.xom.Node;
import nu.xom.Nodes;

public class SA00014Validator extends Validator {

	public SA00014Validator(ProcessContainer files, SimpleValidationResult validationCollector) {
		super(files, validationCollector);
	}

	@Override
	public void validate() {
		validateWsdls();
		validateXsds();
	}

	private void validateWsdls() {
		for (XmlFile wsdl : processContainer.getWsdls()) {
			for (XmlFile peerWsdl : processContainer.getWsdls()) {
				if (wsdl.equals(peerWsdl)) {
					continue;
				}
				if (areSimilar(wsdl, peerWsdl)) {
					compare(wsdl.getDocument(), peerWsdl.getDocument(), "//plink:partnerLinkType");
					compare(wsdl.getDocument(), peerWsdl.getDocument(), "//wsdl:portType");
					compare(wsdl.getDocument(), peerWsdl.getDocument(), "//wsdl:operation");
					compare(wsdl.getDocument(), peerWsdl.getDocument(), "//wsdl:message");
					compare(wsdl.getDocument(), peerWsdl.getDocument(), "//vprop:property");
					compare(wsdl.getDocument(), peerWsdl.getDocument(), "//vprop:propertyAlias");
					compare(wsdl.getDocument(), peerWsdl.getDocument(), "//wsdl:service");
					compare(wsdl.getDocument(), peerWsdl.getDocument(), "//wsdl:binding");
				}
				compareTypes(wsdl, peerWsdl);
			}
			compareTypesToXsds(wsdl);
			detectRedefines(wsdl);
		}
	}

	private void detectRedefines(XmlFile wsdl) {
		Nodes redefine = wsdl.getDocument().query("//xsd:redefine", Standards.CONTEXT);
		if (redefine.hasAny()) {
			addViolation(redefine.get(0));
		}
	}

	private boolean areSimilar(XmlFile file, XmlFile peerFile) {
		return file.getTargetNamespace().equals(peerFile.getTargetNamespace());
	}

	private void compare(Node startNode, Node peerStartNode, String searchString) {
		Nodes nodes = startNode.query(searchString, Standards.CONTEXT);
		Nodes peerNodes = peerStartNode.query(searchString, Standards.CONTEXT);
		for (Node node : nodes) {
			for (Node peerNode : peerNodes) {
				String elementName = new NodeHelper(node).getAttribute("name");
				String peerElementName = new NodeHelper(peerNode).getAttribute("name");
				if (isNameEqual(elementName, peerElementName)) {
					addViolation(node);
				}
			}
		}
	}

	private boolean isNameEqual(String elementName, String peerElementName) {
		return elementName.equals(peerElementName) && !elementName.isEmpty();
	}

	private void compareTypes(XmlFile wsdl, XmlFile peerWsdl) {
		Nodes nodes = wsdl.getDocument().query("//wsdl:types/xsd:schema", Standards.CONTEXT);
		Nodes peerNodes = peerWsdl.getDocument().query("//wsdl:types/xsd:schema", Standards.CONTEXT);
		for (Node node : nodes) {
			for (Node peerNode : peerNodes) {
				compareSchemas(node, peerNode);
			}
		}
	}

	private void compareSchemas(Node node, Node peerNode) {
		String nameSpace = new NodeHelper(node).getAttribute("targetNamespace");
		String peerNameSpace = new NodeHelper(peerNode).getAttribute("targetNamespace");
		if (isNameEqual(nameSpace, peerNameSpace)) {
			compare(node, peerNode, "./xsd:*");
		}
	}


	private void compareTypesToXsds(XmlFile wsdl) {
		Nodes nodes = wsdl.getDocument().query("//wsdl:types/xsd:schema", Standards.CONTEXT);
		for (Node node : nodes) {
			for (XmlFile schema : processContainer.getXsds()) {
				for (Node peerNode : schema.getDocument().query("//xsd:schema", Standards.CONTEXT)) {
					compareSchemas(node, peerNode);
				}
			}
		}
	}

	private void validateXsds() {
		for (XmlFile xsd : processContainer.getXsds()) {
			detectRedefines(xsd);
			for (XmlFile peerXsd : processContainer.getXsds()) {
				if (xsd.equals(peerXsd)) {
					continue;
				}
				if (areSimilar(xsd, peerXsd)) {
					compare(xsd.getDocument(), peerXsd.getDocument(), "/xsd:schema/xsd:*");
				}
			}
		}
	}

	@Override
	public int getSaNumber() {
		return 14;
	}

}
