package isabel.model.bpel;

import isabel.model.NodeHelper;
import isabel.model.Standards;
import nu.xom.Node;

public class ScopeElement extends NodeHelper {

    public ScopeElement(Node node) {
        super(node);
    }

	public boolean hasCompensationHandler() {
		return toXOM().query("./bpel:compensationHandler", Standards.CONTEXT).hasAny();
	}
}
