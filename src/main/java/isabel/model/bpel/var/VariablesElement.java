package isabel.model.bpel.var;

import isabel.model.ContainerAwareReferable;
import isabel.model.NodeHelper;
import isabel.model.ProcessContainer;
import nu.xom.Node;

import java.util.LinkedList;
import java.util.List;

import static isabel.model.Standards.CONTEXT;

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
