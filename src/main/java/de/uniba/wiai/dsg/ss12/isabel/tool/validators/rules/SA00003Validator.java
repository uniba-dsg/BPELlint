package de.uniba.wiai.dsg.ss12.isabel.tool.validators.rules;

import de.uniba.wiai.dsg.ss12.isabel.tool.impl.ValidationCollector;
import de.uniba.wiai.dsg.ss12.isabel.tool.helper.NodeHelper;
import de.uniba.wiai.dsg.ss12.isabel.tool.helper.NodesUtil;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.BpelProcessFiles;
import nu.xom.Node;
import nu.xom.Nodes;

import java.util.ArrayList;
import java.util.List;

import static de.uniba.wiai.dsg.ss12.isabel.tool.impl.Standards.CONTEXT;

public class SA00003Validator extends Validator {

    public SA00003Validator(BpelProcessFiles files,
	                        ValidationCollector violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public void validate() {
        List<Node> processAndScopeNodes = new ArrayList<>();
        processAndScopeNodes.add(getBpelProcessNode());
        processAndScopeNodes.addAll(getScopes());

		for (Node processOrScope : processAndScopeNodes) {
			if (hasExitOnStandardFault("yes", processOrScope)
					&& isCatchingStandardFaults(processOrScope)) {
				addViolation(processOrScope);
			}
		}
	}

    private List<Node> getScopes() {
        return NodesUtil.toList(fileHandler.getBpel().getDocument().query("//bpel:scope", CONTEXT));
    }

    private Node getBpelProcessNode() {
		return fileHandler.getBpel().getDocument().query("/bpel:*", CONTEXT)
				.get(0);
	}

	private boolean hasExitOnStandardFault(String bool, Node enclosingScopes) {
		String exitOnStandardFault = new NodeHelper(enclosingScopes).getAttribute("exitOnStandardFault");
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
			String attribute = new NodeHelper(catchNode).getAttribute("faultName");
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
