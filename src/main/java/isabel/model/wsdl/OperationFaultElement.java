package isabel.model.wsdl;

import static isabel.model.Standards.CONTEXT;
import nu.xom.Node;
import nu.xom.Nodes;
import isabel.model.ContainerAwareReferable;
import isabel.model.NavigationException;
import isabel.model.NodeHelper;
import isabel.model.PrefixHelper;
import isabel.model.ProcessContainer;

public class OperationFaultElement extends ContainerAwareReferable implements OperationMessage {

	private final NodeHelper fault;

	public OperationFaultElement(Node fault, ProcessContainer processContainer) {
		super(fault, processContainer);
		this.fault = new NodeHelper(fault,"fault");
	}
	
	@Override
	public MessageElement getMessage() throws NavigationException {
		String messageName = fault.getAttribute("message");
		Nodes messageNodes = this.toXOM().query(
				"//wsdl:message[@name='" + PrefixHelper.removePrefix(messageName) + "']", CONTEXT);
		if (!messageNodes.hasAny()) {
			throw new NavigationException("Message " + messageName + "is not defined for <fault>.");
		}
		
		return new MessageElement(messageNodes.get(0), getProcessContainer());
	}

}
