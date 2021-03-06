package bpellint.core.validators.rules;

import api.SimpleValidationResult;
import bpellint.core.model.NavigationException;
import bpellint.core.model.ProcessContainer;
import bpellint.core.model.bpel.var.FromElement;
import nu.xom.Document;
import nu.xom.Node;
import nu.xom.Nodes;

import static bpellint.core.model.Standards.CONTEXT;

public class SA00035Validator extends Validator {

    private static final int NO_MY_ROLE_ATTRIBUTE = 1;
    private static final int PARTNER_LINK_IS_MISSING = 2;
    private int errorType = NO_MY_ROLE_ATTRIBUTE;

    public SA00035Validator(ProcessContainer files, SimpleValidationResult validationCollector) {
        super(files, validationCollector);
    }

    @Override
    public void validate() {
        Nodes endpointReferenceFroms = getEndpointReferenceFroms();
        for (Node fromNode : endpointReferenceFroms) {
            if (!correspondingPartnerLinkHasMyRole(new FromElement(fromNode, processContainer))) {
                addViolation(fromNode, errorType);
            }
        }
    }

    private Nodes getEndpointReferenceFroms() {
        Document bpelDocument = processContainer.getBpel().getDocument();
        return bpelDocument.query("//bpel:from[@endpointReference='myRole']", CONTEXT);
    }

    private boolean correspondingPartnerLinkHasMyRole(FromElement from) {
        try {
            return from.getPartnerLink().hasMyRole();
        } catch (NavigationException e) {
            errorType = PARTNER_LINK_IS_MISSING;
            return false;
        }
    }

    @Override
    public int getSaNumber() {
        return 35;
    }

}
