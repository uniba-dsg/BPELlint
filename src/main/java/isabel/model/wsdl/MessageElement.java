package isabel.model.wsdl;

import java.util.LinkedList;
import java.util.List;

import nu.xom.Node;
import nu.xom.Nodes;
import isabel.model.ComparableNode;
import isabel.model.NavigationException;
import isabel.model.NodeHelper;
import isabel.model.Referable;
import isabel.model.Standards;

public class MessageElement implements Referable {

	private final NodeHelper message;

	public MessageElement(Node node) {
		message = new NodeHelper(node, "message");
	}
	
	public boolean hasAnyPart() {
		try {
			return !getParts().isEmpty();
		} catch (NavigationException e) {
			return false;
		}
	}
	
	public boolean hasOnlyOnePart() {
		try {
			return getParts().size() == 1;
		} catch (NavigationException e) {
			return false;
		}
	}

	public PartElement getSinglePart() {
		try {
			return getParts().get(0);
		} catch (NavigationException e) {
			return null;
		}
	}

	private List<PartElement> getParts() throws NavigationException {
		Nodes parts = toXOM().query("./wsdl:part", Standards.CONTEXT);
		if(!parts.hasAny()){
			throw new NavigationException("<message> has no parts");
		}
		List<PartElement> partElements = new LinkedList<>();
		for (Node part : parts) {
			partElements.add(new PartElement(part));
		}
		return partElements;
	}
	
	@Override
	public boolean equals(Object object) {
		if(object == null || !(object instanceof MessageElement)){
			return false;
		}
		
		MessageElement messageElement = (MessageElement) object;
		return new ComparableNode(toXOM()).equals(new ComparableNode(messageElement.toXOM()));
	}
	
	@Override
	public Node toXOM() {
		return message.toXOM();
	}

	
}
