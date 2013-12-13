package isabel.model.bpel;

import isabel.model.NodeHelper;
import nu.xom.Node;

import static isabel.model.Standards.CONTEXT;

public class VariableElement extends NodeHelper {

    public VariableElement(Node node) {
        super(node);

        // node can be either an OnEvent as well as a variable
    }

    public boolean hasMessageType() {
        return node.query("@messageType", CONTEXT).hasAny();
    }

    public boolean hasType() {
        return node.query("@type", CONTEXT).hasAny();
    }

    public boolean hasElement() {
        return node.query("@element", CONTEXT).hasAny();
    }

    public String getName() {
        return getAttribute("name");
    }

    public String getType() {
        return getAttribute("type");
    }

    public String getMessageType() {
        return getAttribute("messageType");
    }
}
