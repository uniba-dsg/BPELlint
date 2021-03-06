package bpellint.core.model.bpel.var;

import bpellint.core.model.ContainerAwareReferable;
import bpellint.core.model.NavigationException;
import bpellint.core.model.NodeHelper;
import bpellint.core.model.ProcessContainer;
import bpellint.core.model.bpel.PartnerLinkElement;
import bpellint.core.model.wsdl.PropertyAliasElement;
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

	@Override
	public PropertyAliasElement getVariablePropertyAlias() throws NavigationException {
		return delegate.getVariablePropertyAlias();
	}

}
