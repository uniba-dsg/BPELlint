package isabel.tool.validators.rules;

import isabel.model.NodeHelper;
import isabel.model.NodesUtil;
import isabel.tool.impl.ValidationCollector;
import isabel.model.ProcessContainer;
import nu.xom.Node;
import nu.xom.Nodes;

import java.util.ArrayList;
import java.util.List;

import static isabel.model.Standards.CONTEXT;

public class SA00003Validator extends Validator {

    public SA00003Validator(ProcessContainer files,
                            ValidationCollector violationCollector) {
        super(files, violationCollector);
    }

    @Override
    public void validate() {
        List<Node> processAndScopeNodes = new ArrayList<>();
        processAndScopeNodes.add(fileHandler.getProcess().asElement());
        processAndScopeNodes.addAll(getScopes());

        for (Node processOrScope : processAndScopeNodes) {
            if (hasExitOnStandardFault("yes", processOrScope)
                    && isCatchingStandardFaults(processOrScope)) {
                addViolation(processOrScope);
            }
        }
    }

    private List<Node> getScopes() {
        return NodesUtil.toList(getAllScopes());
    }

    private Nodes getAllScopes() {
        return fileHandler.getBpel().getDocument().query("//bpel:scope", CONTEXT);
    }

    private boolean hasExitOnStandardFault(String bool, Node enclosingScopes) {
        String exitOnStandardFault = new NodeHelper(enclosingScopes)
                .getAttribute("exitOnStandardFault");
        return bool.equals(exitOnStandardFault);
    }

    private boolean isCatchingStandardFaults(Node currentScope) {
        if (catchesStandardFaultDirectly(currentScope))
            return true;
        boolean foundStandardFault = false;
        for (Node scope : currentScope.query("bpel:scope", CONTEXT)) {
            if (!hasExitOnStandardFault("no", scope)) {
                foundStandardFault |= isCatchingStandardFaults(scope);
            }
        }
        return foundStandardFault;
    }

    private boolean catchesStandardFaultDirectly(Node currentScope) {
        Nodes catches = currentScope.query("bpel:faultHandlers/bpel:catch",
                CONTEXT);
        for (Node catchNode : catches) {
            String attribute = new NodeHelper(catchNode)
                    .getAttribute("faultName");
            for (String fault : BPELFaults.VALUES) {
                if (fault.equals(attribute)) {
                    return true;
                }
            }

        }
        return false;
    }

    @Override
    public int getSaNumber() {
        return 3;
    }
}
