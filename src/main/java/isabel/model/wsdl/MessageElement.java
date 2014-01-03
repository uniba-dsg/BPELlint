package isabel.model.wsdl;

import java.util.LinkedList;
import java.util.List;

import nu.xom.Node;
import nu.xom.Nodes;
import isabel.model.ContainerAwareReferable;
import isabel.model.NavigationException;
import isabel.model.NodeHelper;
import isabel.model.ProcessContainer;
import isabel.model.Standards;

public class MessageElement extends ContainerAwareReferable {

	private final NodeHelper message;

	public MessageElement(Node message, ProcessContainer processContainer) {
		super(message, processContainer);
		this.message = new NodeHelper(message, "message");
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
		Nodes parts = message.toXOM().query("./wsdl:part", Standards.CONTEXT);
		if(!parts.hasAny()){
			throw new NavigationException("<message> has no parts");
		}
		List<PartElement> partElements = new LinkedList<>();
		for (Node part : parts) {
			partElements.add(new PartElement(part, getProcessContainer()));
		}
		return partElements;
	}

}
