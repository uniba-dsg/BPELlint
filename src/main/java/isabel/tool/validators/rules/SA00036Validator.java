package isabel.tool.validators.rules;

import isabel.model.NavigationException;
import isabel.model.ProcessContainer;
import isabel.model.bpel.var.FromElement;
import isabel.tool.impl.ValidationCollector;
import nu.xom.Document;
import nu.xom.Node;
import nu.xom.Nodes;

import static isabel.model.Standards.CONTEXT;

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
            if (!correspondingPartnerLinkHasPartnerRole(new FromElement(from, processContainer))) {
                addViolation(from, errorType);
            }
        }
    }

    private Nodes getEndpoinReferenceFroms() {
        Document bpelDocument = processContainer.getBpel().getDocument();
        return bpelDocument.query("//bpel:from[@endpointReference='partnerRole']", CONTEXT);
    }

    private boolean correspondingPartnerLinkHasPartnerRole(FromElement from) {
        try {
            return from.getPartnerLink().hasPartnerRole();
        } catch (NavigationException e) {
            errorType = PARTNER_LINK_IS_MISSING;
            return false;
        }
    }

    @Override
    public int getSaNumber() {
        return 36;
    }

}
