package isabel.model.bpel.var;

import isabel.model.ContainerAwareReferable;
import isabel.model.NavigationException;
import isabel.model.NodeHelper;
import isabel.model.ProcessContainer;
import isabel.model.bpel.PartnerLinkElement;
import nu.xom.Node;

public class ToElement extends ContainerAwareReferable implements CopyEntity {

    private final CopyEntity delegate;

	public ToElement(Node to, ProcessContainer processContainer) {
        super(to, processContainer);
        new NodeHelper(to, "to");
        delegate = new CopyEntityImpl(to, processContainer);
    }

	@Override
	public boolean isEmpty() {
		return delegate.isEmpty();
	}

	@Override
	public boolean isMessageVariableAssignment() {
		return delegate.isMessageVariableAssignment();
	}

	@Override
	public boolean isPartnerLinkAssignment() {
		return delegate.isPartnerLinkAssignment();
	}

	@Override
	public boolean isVariableAssignment() {
		return delegate.isVariableAssignment();
	}

	@Override
	public boolean isQueryResultAssignment() {
		return delegate.isQueryResultAssignment();
	}

	@Override
	public boolean isLiteralAssignment() {
		return delegate.isLiteralAssignment();
	}

	@Override
	public PartnerLinkElement getPartnerLink() throws NavigationException {
		return delegate.getPartnerLink();
	}

}
