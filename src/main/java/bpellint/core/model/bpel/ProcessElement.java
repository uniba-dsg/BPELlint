package bpellint.core.model.bpel;

import bpellint.core.model.NodeHelper;
import bpellint.core.model.ProcessContainer;
import nu.xom.Node;

public class ProcessElement extends ScopeElement {

	public ProcessElement(Node node, ProcessContainer processContainer) {
		super(node, processContainer);
	}

	public ProcessElement(NodeHelper nodeHelper, ProcessContainer processContainer) {
		this(nodeHelper.toXOM(), processContainer);
	}

}
