package isabel.model.wsdl;

import static isabel.model.Standards.CONTEXT;
import nu.xom.Node;
import nu.xom.Nodes;
import isabel.model.NavigationException;
import isabel.model.NodeHelper;
import isabel.model.Referable;

public class OperationFaultElement implements OperationMessage, Referable{

	private final NodeHelper fault;

	public OperationFaultElement(Node node) {
		fault = new NodeHelper(node,"fault");
	}
	
	@Override
	public MessageElement getMessage() throws NavigationException {
		String messageName = fault.getAttribute("message");
		Nodes messageNodes = this.toXOM().query(
				"//wsdl:message[@name='" + messageName + "']", CONTEXT);
		if (!messageNodes.hasAny()) {
			throw new NavigationException("Message " + messageName + "is not defined for <fault>.");
		}
		
		return new MessageElement(messageNodes.get(0));
	}
	
	@Override
	public Node toXOM() {
		return fault.toXOM();
	}

}
