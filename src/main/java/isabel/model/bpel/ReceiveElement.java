package isabel.model.bpel;

import isabel.model.NodeHelper;
import nu.xom.Node;

public class ReceiveElement extends NodeHelper implements StartActivity{

	public ReceiveElement(Node receive) {
		super(receive);

		if (!getLocalName().equals("receive")) {
			throw new IllegalArgumentException(
					"receive helper only works for bpel:receive elements");
		}
	}

	public boolean hasFromParts() {
		return hasQueryResult("bpel:fromParts");
	}

	public boolean hasVariable() {
		return hasAttribute("variable");
	}

    @Override
    public boolean isStartActivity() {
        return hasAttribute("createInstance") && "true".equals(getAttribute("createInstance"));
    }

}
