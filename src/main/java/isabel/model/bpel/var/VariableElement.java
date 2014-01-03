package isabel.model.bpel.var;

import isabel.model.ContainerAwareReferable;
import isabel.model.NodeHelper;
import isabel.model.ProcessContainer;
import nu.xom.Node;

public class VariableElement extends ContainerAwareReferable implements VariableLike {

	private NodeHelper variable;
	private VariableLikeImpl variableDelegate;

	public VariableElement(Node variable, ProcessContainer processContainer) {
        super(variable, processContainer);
        this.variable = new NodeHelper(variable,"variable");
        this.variableDelegate = new VariableLikeImpl(variable, processContainer);
    }


    public boolean hasType() {
        return variable.hasAttribute("type");
    }

	@Override
    public boolean hasVariableMessageType() {
        return variableDelegate.hasVariableMessageType();
    }
	
    @Override
    public boolean hasVariableElement() {
        return variableDelegate.hasVariableElement();
    }

	@Override
	public String getVariableElement() {
		return variableDelegate.getVariableElement();
	}
	
	@Override
    public String getVariableName() {
        return variableDelegate.getVariableName();
    }

    public String getType() {
        return variableDelegate.getType();
    }
    
    @Override
    public String getVariableMessageType() {
        return variableDelegate.getVariableMessageType();
    }

}
