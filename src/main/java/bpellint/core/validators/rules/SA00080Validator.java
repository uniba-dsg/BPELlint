package bpellint.core.validators.rules;

import api.SimpleValidationResult;
import bpellint.core.model.ProcessContainer;
import bpellint.core.model.bpel.fct.FaultHandlersElement;

public class SA00080Validator extends Validator {

    public SA00080Validator(ProcessContainer files,
                            SimpleValidationResult validationCollector) {
        super(files, validationCollector);
    }

    @Override
    public void validate() {
        for (FaultHandlersElement faultHandler : processContainer.getAllFaultHandlerContainers()) {
            if (!faultHandler.hasCatches() && !faultHandler.hasCatchAll()) {
                addViolation(faultHandler);
            }
        }
    }

    @Override
    public int getSaNumber() {
        return 80;
    }
}
