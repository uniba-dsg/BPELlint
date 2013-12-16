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
	public Node toXOM() {
		return message.toXOM();
	}
	
}
