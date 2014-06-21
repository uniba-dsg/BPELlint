package bpellint.model.bpel.mex;

import bpellint.model.ContainerAwareReferable;
import bpellint.model.NodeHelper;
import bpellint.model.ProcessContainer;
import nu.xom.Node;

public class MessageExchangeElement extends ContainerAwareReferable{

	private final NodeHelper messageExchange;

	public MessageExchangeElement(Node messageExchange, ProcessContainer processContainer) {
		super(messageExchange, processContainer);
		this.messageExchange = new NodeHelper(messageExchange, "messageExchange");
	}

	public String getNameAttribute() {
		return messageExchange.getAttribute("name");
	}
}
