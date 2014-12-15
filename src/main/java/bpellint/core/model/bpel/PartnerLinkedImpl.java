package bpellint.core.model.bpel;

import static bpellint.core.model.Standards.CONTEXT;
import bpellint.core.model.ContainerAwareReferable;
import bpellint.core.model.NavigationException;
import bpellint.core.model.ProcessContainer;
import bpellint.core.model.Referable;
import bpellint.core.model.bpel.mex.OnEventElement;
import nu.xom.Nodes;

public class PartnerLinkedImpl extends ContainerAwareReferable implements PartnerLinked{

	private final String partnerLinkName;
	private final ScopeElement startScope;

	public PartnerLinkedImpl(Referable element, ProcessContainer processContainer, String partnerLinkName) {
		super(element.toXOM(), processContainer);
		this.partnerLinkName = partnerLinkName;
		if (element instanceof OnEventElement) {
			startScope = ((OnEventElement) element).getAssociatedScope();
		} else {
			startScope = getEnclosingScope();
		}
	}

	@Override
	public PartnerLinkElement getPartnerLink() throws NavigationException {
		return getPartnerLink(startScope);
	}

	private PartnerLinkElement getPartnerLink(ScopeElement scope) throws NavigationException {
		Nodes partnerLink = scope.toXOM().query("bpel:partnerLinks/bpel:partnerLink[@name='" + partnerLinkName + "']", CONTEXT);
		if (!partnerLink.hasAny()){
			if (scope instanceof ProcessElement) {
				throw new NavigationException("PartnerLink not defined");
			}
			return getPartnerLink(scope.getEnclosingScope());
		}
		return new PartnerLinkElement(partnerLink.get(0), getProcessContainer());
	}

}
