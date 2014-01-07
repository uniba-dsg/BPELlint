package isabel.model.bpel;

import isabel.model.ContainerAwareReferable;
import isabel.model.NavigationException;
import isabel.model.NodeHelper;
import isabel.model.ProcessContainer;
import isabel.model.Standards;
import nu.xom.Node;
import nu.xom.Nodes;

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

	public String getCorrelationSetAttribute() {
		return correlation.getAttribute("set");
	}

	public CorrelationSetElement getCorrelationSet(ScopeElement startScope)
			throws NavigationException {
		Nodes correlationSet = startScope.toXOM().query(
				"./bpel:correlationSets/bpel:correlationSet[@name='" + getCorrelationSetAttribute() + "']",
				Standards.CONTEXT);
		if (!correlationSet.hasAny()) {
			if (startScope instanceof ProcessElement) {
				throw new NavigationException("<correlationSet> with name "
						+ getCorrelationSetAttribute() + " is missing");
			} else {
				return getCorrelationSet(startScope.getEnclosingScope());
			}
		}
		return new CorrelationSetElement(correlationSet.get(0), getProcessContainer());
	}
}
