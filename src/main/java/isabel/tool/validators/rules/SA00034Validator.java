package isabel.tool.validators.rules;

import isabel.model.bpel.VariableElement;
import isabel.model.NodeHelper;
import isabel.model.NodesUtil;
import isabel.model.NavigationException;
import isabel.tool.impl.ValidationCollector;
import isabel.model.ProcessContainer;
import nu.xom.Node;
import nu.xom.Nodes;

import java.util.List;

import static isabel.model.Standards.CONTEXT;

public class SA00034Validator extends Validator {

    private static final int VARIABLE_IS_MISSING = 2;
    private static final int PART_IS_PROHIBITED_FOR_NON_MESSAGE_TYPES = 1;

    public SA00034Validator(ProcessContainer files,
                            ValidationCollector validationCollector) {
        super(files, validationCollector);
    }

    @Override
    public void validate() {
        List<Node> fromTos = getFroms();
        fromTos.addAll(getTos());

        checkPartAttributeUsage(fromTos);
    }

    private List<Node> getTos() {
        Nodes toNodes = fileHandler.getBpel().getDocument()
                .query("//bpel:to[@variable]", CONTEXT);
        return NodesUtil.toList(toNodes);
    }

    private List<Node> getFroms() {
        Nodes fromNodes = fileHandler.getBpel().getDocument()
                .query("//bpel:copy/bpel:from[@variable]", CONTEXT);
        return NodesUtil.toList(fromNodes);
    }

    private void checkPartAttributeUsage(List<Node> fromTos) {
        for (Node fromToNode : fromTos) {
            NodeHelper fromTo = new NodeHelper(fromToNode);
            String variableName = fromTo.getAttribute("variable");
            if (!isCorrespondingVariableOfMessageType(fromToNode, variableName)
                    && fromTo.hasAttribute("part")) {
                addViolation(fromToNode, PART_IS_PROHIBITED_FOR_NON_MESSAGE_TYPES);
            }
        }
    }

    private boolean isCorrespondingVariableOfMessageType(Node fromTo, String variableName) {
        try {
            VariableElement variable = new NodeHelper(fromTo).getVariableByName(variableName);
            if ("catch".equals(variable.getLocalName())) {
                return variable.hasAttribute("faultMessageType");
            }

            return variable.hasMessageType();
        } catch (NavigationException e) {
            addViolation(fromTo, VARIABLE_IS_MISSING);
            return false;
        }
    }

    @Override
    public int getSaNumber() {
        return 34;
    }

}
