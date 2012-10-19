package de.uniba.wiai.dsg.ss12.isabel.tool.helper;

import nu.xom.Node;

public class ReceiveHelper extends NodeHelper {

	public ReceiveHelper(Node receive) {
		super(receive);

        if(!getLocalName().equals("receive")){
            throw new IllegalArgumentException("receive helper only works for bpel:receive elements");
        }
	}

	public boolean hasFromParts() {
		return hasQueryResult("bpel:fromParts");
	}

	public boolean hasVariable() {
		return hasAttribute("variable");
	}
}
