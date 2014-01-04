package isabel.model.bpel;

import isabel.model.ContainerAwareReferable;
import isabel.model.ProcessContainer;
import nu.xom.Node;

import java.util.LinkedList;
import java.util.List;

import static isabel.model.Standards.CONTEXT;

public class PartnerLinksElement extends ContainerAwareReferable {

	public PartnerLinksElement(Node partnerLinks, ProcessContainer processContainer) {
        super(partnerLinks, processContainer);
    }

    public List<PartnerLinkElement> getPartnerLinks() {

        List<PartnerLinkElement> result = new LinkedList<>();

        for (Node node : toXOM().query("bpel:partnerLink", CONTEXT)) {
            result.add(new PartnerLinkElement(node, getProcessContainer()));
        }

        return result;
    }
}
