package isabel.model.bpel;

import isabel.tool.helper.NodeHelper;
import nu.xom.Node;

public class VariableElement extends NodeHelper {

    public VariableElement(Node node) {
        super(node);

        // node can be either an OnEvent as well as a variable
    }

}
