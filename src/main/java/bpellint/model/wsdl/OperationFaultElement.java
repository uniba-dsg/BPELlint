package bpellint.model.wsdl;

import static bpellint.model.Standards.CONTEXT;
import bpellint.model.ContainerAwareReferable;
import bpellint.model.NavigationException;
import bpellint.model.NodeHelper;
import bpellint.model.PrefixHelper;
import bpellint.model.ProcessContainer;
import nu.xom.Node;
import nu.xom.Nodes;

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
