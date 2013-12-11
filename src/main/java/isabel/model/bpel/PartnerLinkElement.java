package isabel.model.bpel;

import isabel.model.NodeHelper;
import nu.xom.Node;

public class PartnerLinkElement extends NodeHelper {

    public PartnerLinkElement(Node node) {
        super(node);
    }

    public boolean hasPartnerRole() {
        return hasAttribute("partnerRole");
    }

    public boolean hasMyRole() {
        return hasAttribute("myRole");
    }

    public boolean hasInitializePartnerRole() {
        return hasAttribute("initializePartnerRole");
    }
}
