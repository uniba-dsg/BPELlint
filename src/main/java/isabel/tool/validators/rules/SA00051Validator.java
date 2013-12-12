package isabel.tool.validators.rules;

import isabel.model.bpel.mex.InvokeElement;
import isabel.tool.impl.ValidationCollector;
import isabel.model.ProcessContainer;
import nu.xom.Node;
import nu.xom.Nodes;

import static isabel.model.Standards.CONTEXT;

public class SA00051Validator extends Validator {

	public SA00051Validator(ProcessContainer files,
	                        ValidationCollector violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public void validate() {

		for (InvokeElement invoke : fileHandler.getAllInvokes()) {

			Nodes toPartsSet = invoke.toXOM().query("bpel:toParts", CONTEXT);
			Nodes inputVariableSet = invoke.toXOM().query("@inputVariable", CONTEXT);
			if (toPartsSet.hasAny() && inputVariableSet.hasAny()) {
				addViolation(invoke);
			}
		}
	}

	@Override
	public int getSaNumber() {
		return 51;
	}

}
