package isabel.model.bpel.var;

import isabel.model.ContainerAwareReferable;
import isabel.model.NodeHelper;
import isabel.model.ProcessContainer;
import nu.xom.Node;

public class VariableLikeImpl extends ContainerAwareReferable implements VariableLike {

	private final NodeHelper variableLike;

	public VariableLikeImpl(Node variableLike, ProcessContainer processContainer) {
		super(variableLike, processContainer);
		this.variableLike = new NodeHelper(variableLike);
	}
	
	@Override
    public boolean hasVariableMessageType() {
        return variableLike.hasAttribute("messageType");
    }

    @Override
    public boolean hasVariableElement() {
        return variableLike.hasAttribute("element");
    }

	@Override
	public String getVariableElement() {
		return variableLike.getAttribute("element");
	}
	
	@Override
    public String getVariableName() {
        return variableLike.getAttribute("name");
    }

    public String getType() {
        return variableLike.getAttribute("type");
    }
    
    @Override
    public String getVariableMessageType() {
        return variableLike.getAttribute("messageType");
    }

}
