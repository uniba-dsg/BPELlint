package bpellint.model.bpel;

import nu.xom.Node;
import nu.xom.Nodes;
import bpellint.model.ContainerAwareReferable;
import bpellint.model.NavigationException;
import bpellint.model.NodeHelper;
import bpellint.model.ProcessContainer;
import bpellint.model.Standards;
import bpellint.model.bpel.mex.MessageActivity;
import bpellint.model.bpel.mex.MessageActivityImpl;

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

	public MessageActivity getCorrespondingMessageActivity() {
		return new MessageActivityImpl(new NodeHelper(toXOM().getParent().getParent()), getProcessContainer());
	}
}
