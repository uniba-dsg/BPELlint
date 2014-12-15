package bpellint.core.model.bpel.var;

import nu.xom.Node;

import java.util.LinkedList;
import java.util.List;

import bpellint.core.model.ContainerAwareReferable;
import bpellint.core.model.NodeHelper;
import bpellint.core.model.ProcessContainer;

import static bpellint.core.model.Standards.CONTEXT;

public class VariablesElement extends ContainerAwareReferable {

    public VariablesElement(Node node, ProcessContainer processContainer) {
        super(node, processContainer);
        new NodeHelper(node, "variables");
    }

    public List<VariableElement> getVariables() {
        List<VariableElement> result = new LinkedList<>();

        for (Node node : toXOM().query("bpel:variable", CONTEXT)) {
            result.add(new VariableElement(node, getProcessContainer()));
        }

        return result;
    }
}
