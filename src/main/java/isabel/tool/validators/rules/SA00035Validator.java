package isabel.tool.validators.rules;

import static isabel.model.Standards.CONTEXT;
import isabel.model.NodeHelper;
import isabel.model.NavigationException;
import isabel.tool.impl.ValidationCollector;
import isabel.model.ProcessContainer;
import nu.xom.Document;
import nu.xom.Node;
import nu.xom.Nodes;

public class SA00035Validator extends Validator {

	private static final int NO_MY_ROLE_ATTRIBUTE = 1;
	private static final int PARTNER_LINK_IS_MISSING = 2;
	private int errorType = NO_MY_ROLE_ATTRIBUTE;

	public SA00035Validator(ProcessContainer files, ValidationCollector validationCollector) {
		super(files, validationCollector);
	}

	@Override
	public void validate() {
		Nodes endpointReferenceFroms = getEndpoinReferenceFroms();
		for (Node from : endpointReferenceFroms) {
			if (!correspondingPartnerLinkHasMyRole(from)) {
				addViolation(from, errorType);
			}
		}
	}

	private Nodes getEndpoinReferenceFroms() {
		Document bpelDocument = fileHandler.getBpel().getDocument();
		Nodes endpointReferenceFroms = bpelDocument.query(
				"//bpel:from[@endpointReference='myRole']", CONTEXT);
		return endpointReferenceFroms;
	}

	private boolean correspondingPartnerLinkHasMyRole(Node from) {
		try {
			String partnerLinkName = new NodeHelper(from).getAttribute("partnerLink");
			Node partnerLink = navigator.getPartnerLink(from.getDocument(), partnerLinkName);
			return hasMyRole(partnerLink);
		} catch (NavigationException e) {
			errorType = PARTNER_LINK_IS_MISSING;
			return false;
		}
	}

	private boolean hasMyRole(Node partnerLink) {
		return new NodeHelper(partnerLink).hasAttribute("myRole");
	}

	@Override
	public int getSaNumber() {
		return 35;
	}

}
