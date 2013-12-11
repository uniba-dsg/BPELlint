package isabel.tool.validators.rules;

import isabel.model.NodeHelper;
import isabel.model.bpel.VariableElement;
import nu.xom.Node;

import java.util.LinkedList;
import java.util.List;

import static isabel.model.Standards.CONTEXT;

public class VariablesElement extends NodeHelper {

    public VariablesElement(Node node) {
        super(node);
    }

    List<VariableElement> getVariables() {
        List<VariableElement> result = new LinkedList<>();

        for (Node node : asElement().query("bpel:variable", CONTEXT)) {
            result.add(new VariableElement(node));
        }

        return result;
    }
}
