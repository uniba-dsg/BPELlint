package bpellint.core.model.bpel.var;

import nu.xom.Node;
import bpellint.core.model.ContainerAwareReferable;
import bpellint.core.model.NodeHelper;
import bpellint.core.model.ProcessContainer;

public class VariableElement extends ContainerAwareReferable implements VariableLike {

	private NodeHelper variable;

	public VariableElement(Node variable, ProcessContainer processContainer) {
        super(variable, processContainer);
        this.variable = new NodeHelper(variable,"variable");
    }

    public boolean hasType() {
        return variable.hasAttribute("type");
    }
}
