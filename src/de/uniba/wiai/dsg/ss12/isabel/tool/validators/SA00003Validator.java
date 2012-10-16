package de.uniba.wiai.dsg.ss12.isabel.tool.validators;

import static de.uniba.wiai.dsg.ss12.isabel.tool.Standards.CONTEXT;
import static de.uniba.wiai.dsg.ss12.isabel.tool.validators.ValidatorNavigator.getAttributeValue;

import java.util.ArrayList;
import java.util.List;

import nu.xom.Node;
import nu.xom.Nodes;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.BpelProcessFiles;
import de.uniba.wiai.dsg.ss12.isabel.tool.reports.ViolationCollector;

public class SA00003Validator extends Validator {

	private String filePath;
	private final List<String> faultList;

	public SA00003Validator(BpelProcessFiles files,
			ViolationCollector violationCollector) {
		super(files, violationCollector);
		faultList = new ArrayList<>();
		faultList.add("bpel:ambiguousReceive");
		faultList.add("bpel:completionConditionFailure");
		faultList.add("bpel:conflictingReceive");
		faultList.add("bpel:conflictingRequest");
		faultList.add("bpel:correlationViolation");
		faultList.add("bpel:invalidBranchCondition ");
		faultList.add("bpel:invalidExpressionValue");
		faultList.add("bpel:invalidVariables");
		faultList.add("bpel:mismatchedAssignmentFailure");
		faultList.add("bpel:missingReply");
		faultList.add("bpel:missingRequest ");
		faultList.add("bpel:scopeInitializationFailure");
		faultList.add("bpel:selectionFailure ");
		faultList.add("bpel:subLanguageExecutionFault");
		faultList.add("bpel:uninitializedPartnerRole");
		faultList.add("bpel:uninitializedVariable");
		faultList.add("bpel:unsupportedReference");
		faultList.add("bpel:xsltInvalidSource");
		faultList.add("bpel:xsltStylesheetNotFound");
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
		String exitOnStandardFault = getAttributeValue(enclosingScopes.query(
				"@exitOnStandardFault", CONTEXT));
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
			String attribute = getAttributeValue(catchNode.query("@faultName",
					CONTEXT));
			for (String fault : faultList) {
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
