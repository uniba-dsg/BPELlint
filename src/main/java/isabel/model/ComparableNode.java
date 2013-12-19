package isabel.model;

import java.util.Objects;

import nu.xom.Attribute;
import nu.xom.Element;
import nu.xom.Node;

public class ComparableNode implements Comparable<ComparableNode>, Referable {

	private final Node node;
	private final String fileName;
	private final int lineNumber;
	private final int columnNumber;

	public ComparableNode(Node node) {
		Objects.requireNonNull(node, "null can not be compared");
		this.node = node;
		fileName = node.getBaseURI();
		lineNumber = getLineNumber(node);
		columnNumber = getColumnNumber(node);
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
	
	public int getLineNumber() {
		return lineNumber;
	}

	public int getColumnNumber() {
		return columnNumber;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + columnNumber;
		result = prime * result
				+ ((fileName == null) ? 0 : fileName.hashCode());
		result = prime * result + lineNumber;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ComparableNode other = (ComparableNode) obj;
		if (columnNumber != other.columnNumber)
			return false;
		if (fileName == null) {
			if (other.fileName != null)
				return false;
		} else if (!fileName.equals(other.fileName))
			return false;
		if (lineNumber != other.lineNumber)
			return false;
		return true;
	}

	@Override
	public int compareTo(ComparableNode comparableNode) {
		if (this.equals(comparableNode)) {
			return 0;
		} else if (!this.fileName.equals(comparableNode.fileName)) {
			throw new IllegalStateException("Nodes are not in the same file");
		} else if (this.lineNumber == comparableNode.lineNumber) {
			return this.columnNumber - comparableNode.columnNumber;
		} else {
			return this.lineNumber - comparableNode.lineNumber;
		}
	}

	@Override
	public Node toXOM() {
		return node;
	}

}
