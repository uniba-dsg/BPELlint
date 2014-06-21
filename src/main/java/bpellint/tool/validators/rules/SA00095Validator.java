package bpellint.tool.validators.rules;

import bpellint.model.ContainerAwareReferable;
import bpellint.model.ProcessContainer;
import bpellint.model.Referable;
import bpellint.model.Standards;
import bpellint.model.bpel.ProcessElement;
import bpellint.model.bpel.mex.OnEventElement;
import bpellint.tool.validators.result.ValidationCollector;
import nu.xom.Node;
import nu.xom.Nodes;

public class SA00095Validator extends Validator {

	private static final int VARIABLE_IS_USED_OUT_OF_ON_EVENT = 1;
	private static final int VARIABLE_IS_USED_HERE_WRONG = 2;
	private String variableName;

	public SA00095Validator(ProcessContainer files,
			ValidationCollector validationCollector) {
		super(files, validationCollector);
	}

	@Override
	public void validate() {
		for (OnEventElement onEvent : processContainer.getAllOnEvents()) {
			this.variableName = onEvent.getVariableName();
			if (onEvent.hasVariableAttribute() && isVariableNotDefinedBefore(onEvent)) {
				if (hasVariableInPeersDefinitions(onEvent.getEnclosingScope(), onEvent)) {
					addViolation(onEvent, VARIABLE_IS_USED_OUT_OF_ON_EVENT);
				}
			}
		}

	}

	private boolean hasVariableInPeersDefinitions(ContainerAwareReferable parent, OnEventElement onEvent) {
		Nodes children = parent.toXOM().query("./*", Standards.CONTEXT);
		for (Node node : children) {
			if (onEvent.equals(node)) {
				continue;
			}
			ContainerAwareReferable childToCheck = new ContainerAwareReferable(node, processContainer);
			if (containsSameVariable(childToCheck)) {
				if (isVariableDefiningScope(childToCheck)) {
					continue;
				}
				return hasVariableInPeersDefinitions(childToCheck, onEvent);
			} else if (containsVariable(node)) {
				addViolation(node, VARIABLE_IS_USED_HERE_WRONG);
				return true;
			}
		}
		return false;
	}

	private boolean containsSameVariable(Referable referable) {
		return referable.toXOM().query(".//"+variableSearchString(), Standards.CONTEXT).hasAny();
	}

	private String variableSearchString() {
		return "bpel:variables/bpel:variable[@name='" + variableName + "']|bpel:onEvent[@variable='" + variableName + "']";
	}

	private boolean isVariableDefiningScope(Referable referable) {
		return referable.toXOM().query(variableSearchString(), Standards.CONTEXT).hasAny();
	}

	private boolean containsVariable(Node node) {
		int lastIndexOfVariable = node.toXML().lastIndexOf(variableName);
		return isInString(lastIndexOfVariable);
	}

	private boolean isInString(int lastIndexOfVariable) {
		return lastIndexOfVariable != -1;
	}

	private boolean isVariableNotDefinedBefore(ContainerAwareReferable referable) {
		if (referable.toXOM().query("bpel:variables/bpel:variable[@name='" + variableName + "']", Standards.CONTEXT).hasAny()) {
			return false;
		}
		if (referable instanceof ProcessElement) {
			return true;
		}
		return isVariableNotDefinedBefore(referable.getEnclosingScope());
	}
	
	@Override
	public int getSaNumber() {
		return 95;
	}

}
