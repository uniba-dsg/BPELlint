package de.uniba.wiai.dsg.ss12.isabel.tool.helper;

import nu.xom.Node;

public class ReceiveHelper extends NodeHelper {

	public ReceiveHelper(Node receive) {
		super(receive);
	}

	public boolean hasFromParts() {
		return hasQueryResult("bpel:fromParts");
	}

	public boolean hasVariable() {
		return hasAttribute("variable");
	}
}
