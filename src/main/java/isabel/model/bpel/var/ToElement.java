package isabel.model.bpel.var;

import isabel.model.NodeHelper;
import isabel.model.ProcessContainer;
import nu.xom.Node;

public class ToElement extends CopyEntityImpl {

    public ToElement(Node node, ProcessContainer processContainer) {
        super(node, processContainer);
        new NodeHelper(node, "to");
    }

}
