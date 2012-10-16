package de.uniba.wiai.dsg.ss12.isabel.tool.validators;

import nu.xom.Element;
import nu.xom.Node;

public class NodeHelper {

	private Node node;

	public NodeHelper(Node node) {
		if(node == null){
			throw new IllegalArgumentException("given node should not be null");
		}

		this.node = node;
	}

	public String getLocalName() {
		Element el = (Element) node;
		return el.getLocalName();
	}

	public boolean hasLocalName(String name) {
		return name.equals(getLocalName());
	}

	public String getTargetNamespace() {
		return ValidatorNavigator.getAttributeValue(node.getDocument().getRootElement()
				.query("@targetNamespace"));
	}

	public boolean hasTargetNamespace(String targetNamespace){
		return getTargetNamespace().equals(targetNamespace);
	}

	public boolean hasTargetNamespace(Node otherNode){
		return hasTargetNamespace(new NodeHelper(otherNode).getTargetNamespace());
	}
}
