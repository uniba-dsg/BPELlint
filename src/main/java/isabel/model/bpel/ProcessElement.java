package isabel.model.bpel;

import isabel.model.NodeHelper;
import nu.xom.Node;

public class ProcessElement extends ScopeElement {

	public ProcessElement(Node node) {
		this(new NodeHelper(node, "process"));
	}

	public ProcessElement(NodeHelper nodeHelper) {
		super(nodeHelper);
	}

}
