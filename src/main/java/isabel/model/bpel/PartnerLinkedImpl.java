package isabel.model.bpel;

import static isabel.model.Standards.CONTEXT;
import nu.xom.Nodes;
import isabel.model.ContainerAwareReferable;
import isabel.model.NavigationException;
import isabel.model.ProcessContainer;
import isabel.model.Referable;
import isabel.model.bpel.mex.OnEventElement;

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
