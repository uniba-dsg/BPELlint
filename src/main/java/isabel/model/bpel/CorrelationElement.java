package isabel.model.bpel;

import isabel.model.NodeHelper;
import isabel.model.Referable;
import nu.xom.Node;

public class CorrelationElement implements Referable {

    private NodeHelper correlation;

	public CorrelationElement(Node node) {
        correlation = new NodeHelper(node, "correlation");
    }

	@Override
	public Node toXOM() {
		return correlation.toXOM();
	}
	
	private String getInitiate() {
		return correlation.getAttribute("initiate");
	}

	public boolean isJoinInitiate() {
		return "join".equals(getInitiate());
	}

	public String getCorrelationSet() {
		return correlation.getAttribute("set");
	}
	
}
