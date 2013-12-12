package isabel.tool.validators.rules;

import static isabel.model.Standards.CONTEXT;
import isabel.model.NodeHelper;
import isabel.model.NavigationException;
import isabel.model.bpel.PartnerLinkElement;
import isabel.tool.impl.ValidationCollector;
import isabel.model.ProcessContainer;
import nu.xom.Document;
import nu.xom.Node;
import nu.xom.Nodes;

public class SA00037Validator extends Validator {

	private static final int NO_PARTNER_ROLE_ATTRIBUTE = 1;
	private static final int PARTNER_LINK_IS_MISSING = 2;
	private int errorType = NO_PARTNER_ROLE_ATTRIBUTE;

	public SA00037Validator(ProcessContainer files, ValidationCollector validationCollector) {
		super(files, validationCollector);
	}

	@Override
	public void validate() {
		Nodes partnerLinkTos = getPartnerLinkTos();
		for (Node to : partnerLinkTos) {
			if (!correspondingPartnerLinkHasPartnerRole(new NodeHelper(to))) {
				addViolation(to, errorType);
			}
		}
	}

	private Nodes getPartnerLinkTos() {
		Document bpelDocument = fileHandler.getBpel().getDocument();
        return bpelDocument.query("//bpel:to[@partnerLink]", CONTEXT);
	}

	private boolean correspondingPartnerLinkHasPartnerRole(NodeHelper to) {
		try {
			String partnerLinkName = to.getAttribute("partnerLink");
			PartnerLinkElement partnerLink = to.getPartnerLink(partnerLinkName);
            return partnerLink.hasPartnerRole();
        } catch (NavigationException e) {
			errorType = PARTNER_LINK_IS_MISSING;
			return false;
		}
	}

    @Override
	public int getSaNumber() {
		return 37;
	}

}
