package bpellint.core.validators.rules;

import java.util.HashSet;
import java.util.Set;

import api.SimpleValidationResult;
import bpellint.core.model.NavigationException;
import bpellint.core.model.ProcessContainer;
import bpellint.core.model.bpel.OptionalElementNotPresentException;
import bpellint.core.model.bpel.flow.FlowElement;
import bpellint.core.model.bpel.flow.LinkedActivity;
import bpellint.core.model.bpel.flow.SourceElement;
import bpellint.core.model.bpel.flow.TargetElement;


public class SA00072Validator extends Validator {

	private Set<String> linkNames;

	public SA00072Validator(ProcessContainer files,
			SimpleValidationResult validationCollector) {
		super(files, validationCollector);
	}

	@Override
	public void validate() {
		for (SourceElement source : processContainer.getAllSources()) {
			linkNames = new HashSet<>();
			linkNames.add(source.getLinkName());
			if (isCreatingControlCycle(source)) {
				addViolation(source);
				break;
			}

		}
	}

	private boolean isCreatingControlCycle(SourceElement source) {
		boolean cyclic = false;
		try {
			FlowElement flow = source.getLink().getFlow();
			TargetElement target = flow.getTargetElement(source.getLinkName());
			LinkedActivity activity = target.getActivity();
			for (SourceElement sourceElement : activity.getSourceElements()) {
				if (isReachedAgain(sourceElement)) {
					return true;
				}
				cyclic = cyclic	|| isCreatingControlCycle(sourceElement);
			}
		} catch (OptionalElementNotPresentException |  NavigationException ignore) {
			// no source => reached an end of the graph => no cycles so far
			// ignore navigation error because a cycle can not exist without a working link
		}

		return cyclic;
	}

	private boolean isReachedAgain(SourceElement sourceElement) {
		return !linkNames.add(sourceElement.getLinkName());
	}

	@Override
	public int getSaNumber() {
		return 72;
	}

}
