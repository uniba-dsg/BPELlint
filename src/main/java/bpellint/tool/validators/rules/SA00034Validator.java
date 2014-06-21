package bpellint.tool.validators.rules;

import nu.xom.Node;
import nu.xom.Nodes;

import java.util.List;

import bpellint.model.NavigationException;
import bpellint.model.NodeHelper;
import bpellint.model.NodesUtil;
import bpellint.model.ProcessContainer;
import bpellint.model.bpel.var.VariableLike;
import bpellint.tool.validators.result.ValidationCollector;

import static bpellint.model.Standards.CONTEXT;

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
        Nodes toNodes = processContainer.getBpel().getDocument()
                .query("//bpel:to[@variable]", CONTEXT);
        return NodesUtil.toList(toNodes);
    }

    private List<Node> getFroms() {
        Nodes fromNodes = processContainer.getBpel().getDocument()
                .query("//bpel:copy/bpel:from[@variable]", CONTEXT);
        return NodesUtil.toList(fromNodes);
    }

    private void checkPartAttributeUsage(List<Node> fromTos) {
        for (Node fromToNode : fromTos) {
            NodeHelper fromTo = new NodeHelper(fromToNode);
            String variableName = fromTo.getAttribute("variable");
            if (!isCorrespondingVariableOfMessageType(fromTo, variableName)
                    && fromTo.hasAttribute("part")) {
                addViolation(fromToNode, PART_IS_PROHIBITED_FOR_NON_MESSAGE_TYPES);
            }
        }
    }

    private boolean isCorrespondingVariableOfMessageType(NodeHelper fromTo, String variableName) {
        try {
            VariableLike variable = navigator.getVariableByName(fromTo, variableName);

            return variable.hasVariableMessageType();
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
