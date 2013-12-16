package isabel.model.wsdl;

import nu.xom.Node;
import isabel.model.NodeHelper;
import isabel.model.Referable;

public class MessageElement implements Referable {

	private final NodeHelper message;

	public MessageElement(Node node) {
		message = new NodeHelper(node, "message");
	}
	
	public boolean hasAnyPart() {
		return toXOM().query("child::*").hasAny();
	}
	
	public boolean hasOnlyOnePart() {
		return toXOM().query("child::*").size() == 1;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((message == null) ? 0 : message.hashCode());
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
		MessageElement other = (MessageElement) obj;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		return true;
	}

	@Override
	public Node toXOM() {
		return message.toXOM();
	}
	
}
