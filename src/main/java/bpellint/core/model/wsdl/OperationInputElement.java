package bpellint.core.model.wsdl;

import static bpellint.core.model.Standards.CONTEXT;
import bpellint.core.model.ContainerAwareReferable;
import bpellint.core.model.NavigationException;
import bpellint.core.model.NodeHelper;
import bpellint.core.model.PrefixHelper;
import bpellint.core.model.ProcessContainer;
import nu.xom.Node;
import nu.xom.Nodes;

public class OperationInputElement extends ContainerAwareReferable implements OperationMessage {

	private final NodeHelper input;

	public OperationInputElement(Node input, ProcessContainer processContainer) {
		super(input, processContainer);
		this.input = new NodeHelper(input, "input");
	}

	@Override
	public MessageElement getMessage() throws NavigationException {
		String messageName = input.getAttribute("message");
		Nodes messageNodes = this.toXOM().query(
				"//wsdl:message[@name='" + PrefixHelper.removePrefix(messageName) + "']", CONTEXT);
		if (!messageNodes.hasAny()) {
			throw new NavigationException("Message " + messageName + "is not defined for <input>.");
		}
		
		return new MessageElement(messageNodes.get(0), getProcessContainer());
	}

}
