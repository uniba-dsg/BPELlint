package bpellint.core.model.bpel.mex;

import bpellint.core.model.ContainerAwareReferable;
import bpellint.core.model.NodeHelper;
import bpellint.core.model.ProcessContainer;
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
