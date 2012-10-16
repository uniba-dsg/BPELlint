package de.uniba.wiai.dsg.ss12.isabel.tool.validators;

import de.uniba.wiai.dsg.ss12.isabel.tool.helper.NodeHelper;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.BpelProcessFiles;
import de.uniba.wiai.dsg.ss12.isabel.tool.reports.ValidationResult;
import nu.xom.Node;
import nu.xom.Nodes;

import java.util.Arrays;
import java.util.List;

import static de.uniba.wiai.dsg.ss12.isabel.tool.Standards.CONTEXT;
import static de.uniba.wiai.dsg.ss12.isabel.tool.validators.ValidatorNavigator.getAttributeValue;

public class SA00003Validator extends Validator {

	private String filePath;

	public SA00003Validator(BpelProcessFiles files,
	                        ValidationResult violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public void validate() {
		filePath = fileHandler.getBpel().getFilePath();
		Node process = getBpelProcessNode();

		if (hasExitOnStandardFault("yes", process)
				&& isCatchingStandardFaults(process)) {
			addViolation(process);
		}

		Nodes scopes = process.query("//bpel:scope", CONTEXT);
		for (Node scope : scopes) {
			if (hasExitOnStandardFault("yes", scope)
					&& isCatchingStandardFaults(scope)) {
				addViolation(scope);
			}
		}
	}

	private Node getBpelProcessNode() {
		return fileHandler.getBpel().getDocument().query("/bpel:*", CONTEXT)
				.get(0);
	}

	private boolean hasExitOnStandardFault(String bool, Node enclosingScopes) {
		String exitOnStandardFault = new NodeHelper(enclosingScopes).getAttributeByName("exitOnStandardFault");
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
			String attribute = new NodeHelper(catchNode).getAttributeByName("faultName");
			for (String fault : BPELFaults.VALUES) {
				if (fault.equals(attribute)) {
					return true;
				}
			}

		}
		return false;
	}

	private void addViolation(Node process) {
		addViolation(filePath, process, 1);
	}

	@Override
	public int getSaNumber() {
		return 3;
	}
}
