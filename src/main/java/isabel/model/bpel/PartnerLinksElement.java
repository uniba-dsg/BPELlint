package isabel.model.bpel;

import isabel.model.NodeHelper;
import nu.xom.Node;

import java.util.LinkedList;
import java.util.List;

import static isabel.model.Standards.CONTEXT;

public class PartnerLinksElement extends NodeHelper{
    public PartnerLinksElement(Node node) {
        super(node);
    }

    public List<PartnerLinkElement> getPartnerLinks() {

        List<PartnerLinkElement> result = new LinkedList<>();

        for (Node node : asElement().query("bpel:partnerLink", CONTEXT)) {
            result.add(new PartnerLinkElement(node));
        }

        return result;
    }
}
