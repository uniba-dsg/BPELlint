package isabel.model.bpel;

import isabel.model.NodeHelper;
import nu.xom.Node;
import static isabel.model.Standards.CONTEXT;

public class VariableElement extends NodeHelper implements VariableLike {

	public VariableElement(Node node) {
        super(node);

        // node can be either an OnEvent as well as a variable
    }

	@Override
    public boolean hasVariableMessageType() {
        return node.query("@messageType", CONTEXT).hasAny();
    }

    public boolean hasType() {
        return node.query("@type", CONTEXT).hasAny();
    }

    @Override
    public boolean hasVariableElement() {
        return node.query("@element", CONTEXT).hasAny();
    }

	@Override
	public String getVariableElement() {
		return getAttribute("element");
	}
	
	@Override
    public String getVariableName() {
        return getAttribute("name");
    }

    public String getType() {
        return getAttribute("type");
    }
    
    @Override
    public String getVariableMessageType() {
        return getAttribute("messageType");
    }

}
