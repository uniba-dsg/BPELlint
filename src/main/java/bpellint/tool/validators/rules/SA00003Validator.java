package bpellint.tool.validators.rules;

import static bpellint.model.Standards.CONTEXT;

import java.util.List;

import bpellint.model.ProcessContainer;
import bpellint.model.bpel.BPELFaults;
import bpellint.model.bpel.ScopeElement;
import bpellint.model.bpel.fct.CatchElement;
import bpellint.model.bpel.mex.InvokeElement;
import bpellint.tool.validators.result.ValidationCollector;

import nu.xom.Node;
import nu.xom.Nodes;

public class SA00003Validator extends Validator {

	public SA00003Validator(ProcessContainer files, ValidationCollector violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public void validate() {
		validateScopes();
		validateInvokes();
	}

	private void validateScopes() {
		List<ScopeElement> allScopes = processContainer.getAllScopes();
		allScopes.add(processContainer.getProcess());
		for (ScopeElement scope : allScopes) {
			if (scope.hasExitOnStandardFault() && catchesStandardFaultDirectly(scope)) {
				addViolation(scope);
			}
		}
	}

	private void validateInvokes() {
		for (InvokeElement invoke : processContainer.getAllInvokes()) {
			if (invoke.getEnclosingScope().hasExitOnStandardFault()
					&& catchesStandardFaultDirectly(invoke)) {
				addViolation(invoke);
			}
		}
	}

	private boolean catchesStandardFaultDirectly(ScopeElement scope) {
		Nodes catches = scope.toXOM().query("bpel:faultHandlers/bpel:catch", CONTEXT);
		return catchesStandardFaultDirectly(catches);
	}

	private boolean catchesStandardFaultDirectly(InvokeElement invoke) {
		Nodes catches = invoke.toXOM().query("bpel:catch", CONTEXT);
		return catchesStandardFaultDirectly(catches);
	}

	private boolean catchesStandardFaultDirectly(Nodes catches) {
		for (Node catchNode : catches) {
			String faultNameAttribute = new CatchElement(catchNode, processContainer)
					.getFaultNameAttribute();
			for (String faultName : BPELFaults.VALUES) {
				if (faultName.equals(faultNameAttribute)) {
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