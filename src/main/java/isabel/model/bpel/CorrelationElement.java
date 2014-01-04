package isabel.model.bpel;

import isabel.model.ContainerAwareReferable;
import isabel.model.NodeHelper;
import isabel.model.ProcessContainer;
import nu.xom.Node;

public class CorrelationElement extends ContainerAwareReferable {

    private final NodeHelper correlation;

	public CorrelationElement(Node correlation, ProcessContainer processContainer) {
		super(correlation, processContainer);
        this.correlation = new NodeHelper(correlation, "correlation");
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
