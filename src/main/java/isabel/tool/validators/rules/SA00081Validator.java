package isabel.tool.validators.rules;

import isabel.model.ProcessContainer;
import isabel.model.bpel.fct.CatchElement;
import isabel.tool.impl.ValidationCollector;

public class SA00081Validator extends Validator {

	public SA00081Validator(ProcessContainer files,
			ValidationCollector validationCollector) {
		super(files, validationCollector);
	}

	@Override
	public void validate() {
		for (CatchElement catchElement : fileHandler.getAllCatch()) {
			if (!hasDefinitType(catchElement)) {
				addViolation(catchElement);
			}
		}
	}

	private boolean hasDefinitType(CatchElement catchElement) {
		return hasFaultMessageTypeVariable(catchElement)
				|| hasFaultElementVariable(catchElement)
				|| hasNoVariable(catchElement);
	}

	private boolean hasFaultMessageTypeVariable(CatchElement catchElement) {
		return catchElement.hasFaultVariable()
				&& catchElement.hasVariableMessageType()
				&& !catchElement.hasVariableElement();
	}

	private boolean hasFaultElementVariable(CatchElement catchElement) {
		return catchElement.hasFaultVariable()
				&& !catchElement.hasVariableMessageType()
				&& catchElement.hasVariableElement();
	}

	private boolean hasNoVariable(CatchElement catchElement) {
		return !catchElement.hasFaultVariable()
				&& !catchElement.hasVariableMessageType()
				&& !catchElement.hasVariableElement();
	}

	@Override
	public int getSaNumber() {
		return 81;
	}

}
