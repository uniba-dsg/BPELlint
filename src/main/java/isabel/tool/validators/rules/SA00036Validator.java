package isabel.tool.validators.rules;

import static isabel.tool.impl.Standards.CONTEXT;
import nu.xom.Document;
import nu.xom.Node;
import nu.xom.Nodes;
import isabel.tool.helper.NodeHelper;
import isabel.tool.impl.NavigationException;
import isabel.tool.impl.ValidationCollector;
import isabel.tool.imports.ProcessContainer;

public class SA00036Validator extends Validator {

	private static final int NO_PARTNER_ROLE_ATTRIBUTE = 1;
	private static final int PARTNER_LINK_IS_MISSING = 2;
	private int errorType = NO_PARTNER_ROLE_ATTRIBUTE;

	public SA00036Validator(ProcessContainer files, ValidationCollector validationCollector) {
		super(files, validationCollector);
	}

	@Override
	public void validate() {
		Nodes endpointReferenceFroms = getEndpoinReferenceFroms();
		for (Node from : endpointReferenceFroms) {
			if (!correspondingPartnerLinkHasPartnerRole(from)) {
				addViolation(from,errorType);
			}
		}
	}

	private Nodes getEndpoinReferenceFroms() {
		Document bpelDocument = fileHandler.getBpel().getDocument();
		Nodes endpointReferenceFroms = bpelDocument.query(
				"//bpel:from[@endpointReference='partnerRole']", CONTEXT);
		return endpointReferenceFroms;
	}

	private boolean correspondingPartnerLinkHasPartnerRole(Node from) {
		try {
			String partnerLinkName = new NodeHelper(from).getAttribute("partnerLink");
			Node partnerLink = navigator.getPartnerLink(from.getDocument(), partnerLinkName);
			return hasPartnerRole(partnerLink);
		} catch (NavigationException e) {
			errorType  = PARTNER_LINK_IS_MISSING;
			return false;
		}
	}

	private boolean hasPartnerRole(Node partnerLink) {
		return new NodeHelper(partnerLink).hasAttribute("partnerRole");
	}
	@Override
	public int getSaNumber() {
		return 36;
	}

}
