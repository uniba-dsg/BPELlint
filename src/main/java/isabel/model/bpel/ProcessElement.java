package isabel.model.bpel;

import isabel.model.NodeHelper;
import nu.xom.Node;

public class ProcessElement extends ScopeElement {

	public ProcessElement(Node node) {
		super(node);
	}

	public ProcessElement(NodeHelper nodeHelper) {
		this(nodeHelper.toXOM());
	}

}
