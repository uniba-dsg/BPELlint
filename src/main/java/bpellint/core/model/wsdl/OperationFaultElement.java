package bpellint.core.model.wsdl;

import static bpellint.core.model.Standards.CONTEXT;
import bpellint.core.model.ContainerAwareReferable;
import bpellint.core.model.NavigationException;
import bpellint.core.model.NodeHelper;
import bpellint.core.model.PrefixHelper;
import bpellint.core.model.ProcessContainer;
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
