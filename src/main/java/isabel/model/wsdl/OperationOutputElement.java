package isabel.model.wsdl;

import static isabel.model.Standards.CONTEXT;
import nu.xom.Node;
import nu.xom.Nodes;
import isabel.model.ContainerAwareReferable;
import isabel.model.NavigationException;
import isabel.model.NodeHelper;
import isabel.model.PrefixHelper;
import isabel.model.ProcessContainer;

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
