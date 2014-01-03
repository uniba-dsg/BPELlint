package isabel.model.wsdl;

import static isabel.model.Standards.CONTEXT;
import nu.xom.Node;
import nu.xom.Nodes;
import isabel.model.ContainerAwareReferable;
import isabel.model.NavigationException;
import isabel.model.NodeHelper;
import isabel.model.PrefixHelper;
import isabel.model.ProcessContainer;

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
