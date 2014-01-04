package isabel.model.bpel;

import static isabel.model.Standards.CONTEXT;
import nu.xom.Node;
import nu.xom.Nodes;
import isabel.model.ContainerAwareReferable;
import isabel.model.NavigationException;
import isabel.model.ProcessContainer;

public class PartnerLinkedImpl extends ContainerAwareReferable implements PartnerLinked{

	private final String partnerLinkName;

	public PartnerLinkedImpl(Node node, ProcessContainer processContainer, String partnerLinkName) {
		super(node, processContainer);
		this.partnerLinkName = partnerLinkName;
	}

	@Override
	public PartnerLinkElement getPartnerLink() throws NavigationException {
		Nodes partnerLink = toXOM().getDocument().query("//bpel:partnerLinks/bpel:partnerLink[@name='" + partnerLinkName + "']", CONTEXT);
        if (!partnerLink.hasAny()){
        	throw new NavigationException("PartnerLink not defined");
        }
        return new PartnerLinkElement(partnerLink.get(0), getProcessContainer());
	}

}
