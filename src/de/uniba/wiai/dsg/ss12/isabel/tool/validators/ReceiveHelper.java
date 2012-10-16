package de.uniba.wiai.dsg.ss12.isabel.tool.validators;

import nu.xom.Node;

import static de.uniba.wiai.dsg.ss12.isabel.tool.Standards.CONTEXT;

public class ReceiveHelper {

	private final Node receive;

	public ReceiveHelper(Node receive) {
		this.receive = receive;
	}

	public boolean hasFromParts() {
		return receive.query("bpel:fromParts", CONTEXT).size() > 0;
	}

	public boolean hasVariable() {
		return receive.query("@variable", CONTEXT).size() > 0;
	}
}
