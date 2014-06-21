package bpellint.model.wsdl;

import static bpellint.model.Standards.CONTEXT;
import bpellint.model.ContainerAwareReferable;
import bpellint.model.NavigationException;
import bpellint.model.NodeHelper;
import bpellint.model.PrefixHelper;
import bpellint.model.ProcessContainer;
import nu.xom.Node;
import nu.xom.Nodes;

public class OperationOutputElement extends ContainerAwareReferable implements OperationMessage {

	private final NodeHelper output;

	public OperationOutputElement(Node output, ProcessContainer processContainer) {
		super(output, processContainer);
		this.output = new NodeHelper(output, "output");
	}

	@Override
	public MessageElement getMessage() throws NavigationException {
		String messageName = output.getAttribute("message");
		Nodes messageNodes = toXOM().query("//wsdl:message[@name='" + PrefixHelper.removePrefix(messageName) + "']", CONTEXT);
		if (!messageNodes.hasAny()) {
			throw new NavigationException("Message " + messageName + "is not defined for <output>.");
		}
		
		return new MessageElement(messageNodes.get(0), getProcessContainer());
	}

}
