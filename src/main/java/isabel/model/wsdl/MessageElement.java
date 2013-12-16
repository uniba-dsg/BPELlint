package isabel.model.wsdl;

import java.util.LinkedList;
import java.util.List;

import nu.xom.Node;
import nu.xom.Nodes;
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
		String messageElementWsdl = new NodeHelper(messageElement.toXOM()).getFilePath();
		String messageWsdl = message.getFilePath();
		boolean equalDocument = messageElementWsdl.equals(messageWsdl);
		boolean equalLineNumber = getLineNumber(messageElement.message) == getLineNumber(message);
    	boolean equalColumNumber = getColumnNumber(messageElement.message) == getColumnNumber(message);
    	
        return equalDocument && equalLineNumber && equalColumNumber;
	}
	
	private int getLineNumber(NodeHelper nodeHelper) {
		return (Integer) nodeHelper.asElement().getUserData("lineNumber");
	}

	private int getColumnNumber(NodeHelper nodeHelper) {
		return (Integer) nodeHelper.asElement().getUserData("columnNumber");
	}
	
	@Override
	public Node toXOM() {
		return message.toXOM();
	}

	
}
