package isabel.model;

import nu.xom.Attribute;
import nu.xom.Element;
import nu.xom.Node;

public class NodeToId {

	private final Node node;

	public NodeToId(Node node) {
		this.node = node;
	}

	public int getLineNumber() {
		return getLineNumber(node);
	}

	public int getColumnNumber() {
		return getColumnNumber(node);
	}

	private int getLineNumber(Node node) {
		if (node instanceof Element) {
			Element element = (Element) node;
			return (Integer) element.getUserData("lineNumber");
		} else if (node instanceof Attribute) {
			return getLineNumber(node.getParent());
		} else {
			throw new IllegalArgumentException("Node need to be an Element or Attribute.");
		}
	}

	private int getColumnNumber(Node node) {
		if (node instanceof Element) {
			Element element = (Element) node;
			return (Integer) element.getUserData("columnNumber");
		} else if (node instanceof Attribute) {
			return getColumnNumber(node.getParent());
		} else {
			throw new IllegalArgumentException("Node need to be an Element or Attribute.");
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || obj instanceof NodeToId) {
			return false;
		}
		NodeToId other = (NodeToId) obj;
		return toId().equals(other.toId());
	}

	public String toId() {
		return node.getBaseURI() + "#" + new Integer(getLineNumber()).toString() + ":"
				+ new Integer(getColumnNumber()).toString() + "_"
				+ new NodeHelper(node).getLocalName();
	}
}
