package bpellint.core.validators.rules;

import api.SimpleValidationResult;
import bpellint.core.model.ProcessContainer;
import bpellint.core.model.wsdl.OperationElement;

public class SA00001Validator extends Validator {

    private static final int SOLICIT_RESPONSE_TYPE = 2;
    private static final int NOTIFICATION_FAULT = 1;

    public SA00001Validator(ProcessContainer files, SimpleValidationResult violationCollector) {
        super(files, violationCollector);
    }

    @Override
    public void validate() {
        for (OperationElement operation : processContainer.getAllOperations()) {
            if (operation.isNotification()) {
                addViolation(operation, NOTIFICATION_FAULT);
            }
            if (operation.isSolicitResponse()) {
                addViolation(operation, SOLICIT_RESPONSE_TYPE);
            }
        }
    }

    @Override
    public int getSaNumber() {
        return 1;
    }

}
