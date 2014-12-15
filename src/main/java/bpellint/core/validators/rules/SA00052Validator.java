package bpellint.core.validators.rules;

import api.SimpleValidationResult;
import bpellint.core.model.ProcessContainer;
import bpellint.core.model.bpel.mex.InvokeElement;

public class SA00052Validator extends Validator {
    public SA00052Validator(ProcessContainer files,
                            SimpleValidationResult violationCollector) {
        super(files, violationCollector);
    }

    @Override
    public void validate() {
        for (InvokeElement invoke : processContainer.getAllInvokes()) {
            if (invoke.hasOutputVariable() && invoke.hasFromParts()) {
                addViolation(invoke);
            }
        }
    }

    @Override
    public int getSaNumber() {
        return 52;
    }
}
