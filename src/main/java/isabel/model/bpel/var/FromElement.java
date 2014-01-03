package isabel.model.bpel.var;

import isabel.model.NodeHelper;
import isabel.model.ProcessContainer;
import nu.xom.Node;

public class FromElement extends CopyEntityImpl {

    public FromElement(Node node, ProcessContainer processContainer) {
        super(node, processContainer);
        new NodeHelper(node, "from");
    }

}
