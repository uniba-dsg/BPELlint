package de.uniba.wiai.dsg.ss12.isabel.tool.helper;

import de.uniba.wiai.dsg.ss12.isabel.tool.impl.Standards;
import nu.xom.Attribute;
import nu.xom.Element;
import nu.xom.Node;
import nu.xom.Nodes;

public class NodeHelper {

	private Node node;

	public NodeHelper(Node node) {
		if (node == null) {
			throw new IllegalArgumentException("given node should not be null");
		}

		this.node = node;
	}

	public String getLocalName() {
		Element el = (Element) node;
		return el.getLocalName();
	}

	public boolean hasLocalName(String name) {
		return getLocalName().equals(name);
	}

	public String getTargetNamespace() {
		return new NodeHelper(node.getDocument().getRootElement()).getAttributeByName("targetNamespace");
	}

	public boolean hasTargetNamespace(String targetNamespace) {
		return getTargetNamespace().equals(targetNamespace);
	}

	public boolean hasTargetNamespace(Node otherNode) {
		return hasTargetNamespace(new NodeHelper(otherNode).getTargetNamespace());
	}

	public String getAttributeByName(String name) {
		Nodes attributes = node.query("@" + name);

		if (attributes.size() > 0) {
			Node attribute = attributes.get(0);
			if (attribute instanceof Attribute) {
				return attribute.getValue();
			}
		}
		return "";
	}

	public boolean hasAttribute(String name) {
		return !getAttributeByName(name).isEmpty();
	}

	public boolean hasNoAttribute(String name){
		return !hasAttribute(name);
	}
	
	public boolean hasQueryResult(String query){
		return node.query(query, Standards.CONTEXT).size() > 0;
	}
}
