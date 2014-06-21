package bpellint.tool.validators.rules;

import bpellint.model.ProcessContainer;
import bpellint.model.bpel.mex.OnMessageElement;
import bpellint.tool.validators.result.ValidationCollector;

public class SA00063Validator extends Validator {

    public SA00063Validator(ProcessContainer files,
                            ValidationCollector violationCollector) {
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
