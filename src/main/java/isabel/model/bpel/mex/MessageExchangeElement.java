package isabel.model.bpel.mex;

import nu.xom.Node;
import isabel.model.ContainerAwareReferable;
import isabel.model.NodeHelper;
import isabel.model.ProcessContainer;

public class MessageExchangeElement extends ContainerAwareReferable{

	private NodeHelper messageExchange;

	public MessageExchangeElement(Node messageExchange, ProcessContainer processContainer) {
		super(messageExchange, processContainer);
		this.messageExchange = new NodeHelper(messageExchange, "messageExchange");
	}

	public String getNameAttribute() {
		return messageExchange.getAttribute("name");
	}
}
