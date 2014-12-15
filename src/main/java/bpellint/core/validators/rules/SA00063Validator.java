package bpellint.core.validators.rules;

import api.SimpleValidationResult;
import bpellint.core.model.ProcessContainer;
import bpellint.core.model.bpel.mex.OnMessageElement;

public class SA00063Validator extends Validator {

    public SA00063Validator(ProcessContainer files,
                            SimpleValidationResult violationCollector) {
        super(files, violationCollector);
    }

    @Override
    public void validate() {
        for (OnMessageElement onMessage : processContainer.getAllOnMessages()) {
            if (onMessage.hasFromParts() && onMessage.hasVariable()) {
                addViolation(onMessage);
            }
        }
    }

    @Override
    public int getSaNumber() {
        return 63;
    }
}
