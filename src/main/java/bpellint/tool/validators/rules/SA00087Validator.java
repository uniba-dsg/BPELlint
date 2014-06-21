package bpellint.tool.validators.rules;

import bpellint.model.NavigationException;
import bpellint.model.PrefixHelper;
import bpellint.model.ProcessContainer;
import bpellint.model.bpel.mex.OnEventElement;
import bpellint.model.wsdl.MessageElement;
import bpellint.tool.validators.result.ValidationCollector;

public class SA00087Validator extends Validator {

	private static final int MESSAGE_TYPE_IS_NOT_OPERATION_MESSAGE_NAME = 1;
	private static final int MESSAGE_HAS_TWO_OR_MORE_PARTS = 2;
	private static final int ELEMENT_IS_NOT_OPERATION_MESSAGE_PART_ELEMENT = 3;

	public SA00087Validator(ProcessContainer files, ValidationCollector validationCollector) {
		super(files, validationCollector);
	}

	@Override
	public void validate() {
		for (OnEventElement onEvent : processContainer.getAllOnEvents()) {
			try {
				checkTypeOfMessage(onEvent);
			} catch (NavigationException e) {
				// ignore if the message does not exist, this is checked elsewhere
			}
		}
	}

	private void checkTypeOfMessage(OnEventElement onEvent) throws NavigationException {
		MessageElement message = onEvent.getOperation().getInput().getMessage();
		if (onEvent.hasVariableElement()) {
			if (message.getParts().size() > 1) {
				addViolation(onEvent, MESSAGE_HAS_TWO_OR_MORE_PARTS);
				return;
			}
			if (!isOfSameType(onEvent.getVariableElement(), message.getSinglePart().getElement())) {
				addViolation(onEvent, ELEMENT_IS_NOT_OPERATION_MESSAGE_PART_ELEMENT);
				return;
			}
		}
		if (!onEvent.hasVariableMessageType()) {
			return;
		}
		if (!isOfSameType(onEvent.getVariableMessageType(), message.getName())) {
			addViolation(onEvent, MESSAGE_TYPE_IS_NOT_OPERATION_MESSAGE_NAME);
		}
	}

	private boolean isOfSameType(String element, String messageElementType) {
		return PrefixHelper.removePrefix(element).equals(PrefixHelper.removePrefix(messageElementType));
	}

	@Override
	public int getSaNumber() {
		return 87;
	}

}
