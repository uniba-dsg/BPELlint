package isabel.tool.validators.rules;

import java.util.List;

import isabel.model.NavigationException;
import isabel.model.ProcessContainer;
import isabel.model.bpel.fct.CatchElement;
import isabel.model.bpel.fct.FaultHandlersElement;
import isabel.tool.validators.result.ValidationCollector;

public class SA00093Validator extends Validator {

	public SA00093Validator(ProcessContainer files,
			ValidationCollector validationCollector) {
		super(files, validationCollector);
	}

	@Override
	public void validate() {
		for (FaultHandlersElement faultHandlers : processContainer.getAllFaultHandlerContainers()) {
			try {
				List<CatchElement> catches = faultHandlers.getCatches();
				for (CatchElement catchElement : catches) {
					checkForCatchSimilarities(catchElement, catches);
				}
			} catch (NavigationException ignore) {
				// ignore faultHandlers without <catch>
			}
		}
	}

	private void checkForCatchSimilarities(CatchElement catchElement,
			List<CatchElement> catches) {
		for (CatchElement peerCatch : catches) {
			if (catchElement.equals(peerCatch)) {
				continue;
			}
			boolean sameFaultElement = hasSameFaultElementType(catchElement, peerCatch);
			boolean sameFaultMessageType = hasSameFaultMessageType(catchElement, peerCatch);
			boolean sameFaultName = hasSameFaultName(catchElement, peerCatch);

			if (sameFaultElement && sameFaultMessageType && sameFaultName) {
				addViolation(catchElement);
			}
		}
	}

	private boolean hasSameFaultElementType(CatchElement catchElement,
			CatchElement peerCatch) {
		if (catchElement.hasVariableElement()) {
			if (!peerCatch.hasVariableElement()) {
				return false;
			}
			if (!catchElement.getVariableElement().equals(peerCatch.getVariableElement())) {
				return false;
			}
		} else {
			if (peerCatch.hasVariableElement()) {
				return false;
			}
		}
		return true;
	}

	private boolean hasSameFaultMessageType(CatchElement catchElement,
			CatchElement peerCatch) {
		if (catchElement.hasVariableMessageType()) {
			if (!peerCatch.hasVariableMessageType()) {
				return false;
			}
			if (!catchElement.getVariableMessageType().equals(peerCatch.getVariableMessageType())) {
				return false;
			}
		} else {
			if (peerCatch.hasVariableMessageType()) {
				return false;
			}
		}
		return true;
	}

	private boolean hasSameFaultName(CatchElement catchElement,
			CatchElement peerCatch) {
		if (catchElement.hasFaultNameAttribute()) {
			if (!peerCatch.hasFaultNameAttribute()) {
				return false;
			}
			if (!catchElement.getFaultNameAttribute().equals(peerCatch.getFaultNameAttribute())) {
				return false;
			}
		} else {
			if (peerCatch.hasFaultNameAttribute()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int getSaNumber() {
		return 93;
	}

}
