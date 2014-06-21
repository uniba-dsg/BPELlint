package bpellint.model.wsdl;

import static bpellint.model.Standards.CONTEXT;
import bpellint.model.ContainerAwareReferable;
import bpellint.model.NavigationException;
import bpellint.model.NodeHelper;
import bpellint.model.PrefixHelper;
import bpellint.model.ProcessContainer;
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
