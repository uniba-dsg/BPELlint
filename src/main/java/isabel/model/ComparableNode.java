package isabel.model;

import java.util.Objects;

import nu.xom.Node;

public class ComparableNode implements Comparable<ComparableNode>, Referable {

	private final Node node;
	private final String fileName;
	private final int lineNumber;
	private final int columnNumber;

	public ComparableNode(Node node) {
		Objects.requireNonNull(node, "null can not be compared");
		this.node = node;
		NodeToId nodeToId = new NodeToId(node);
		fileName = node.getBaseURI();
		lineNumber = nodeToId.getLineNumber();
		columnNumber = nodeToId.getColumnNumber();
	}

	public ComparableNode(Referable referable) {
		this(referable.toXOM());
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
	public boolean equals(Object object) {
		if (object == null) {
			return false;
		} 
		if (object instanceof Referable) {
			return toString().equals(new NodeToId(((Referable) object).toXOM()).toId());
		} else if (object instanceof Node){
			return toString().equals(new NodeToId((Node) object).toId());
		}
		return false;
	}

	@Override
	public String toString() {
		return new NodeToId(node).toId();
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
