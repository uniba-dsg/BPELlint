package de.uniba.wiai.dsg.ss12.isabel.tool.helper.bpel;

import nu.xom.Node;
import de.uniba.wiai.dsg.ss12.isabel.tool.helper.NodeHelper;

public class ReceiveElement extends NodeHelper {

	public ReceiveElement(Node receive) {
		super(receive);

		if (!getLocalName().equals("receive")) {
			throw new IllegalArgumentException(
					"receive helper only works for bpel:receive elements");
		}
	}

	public boolean hasFromParts() {
		return hasQueryResult("bpel:fromParts");
	}

	public boolean hasVariable() {
		return hasAttribute("variable");
	}
}
