package bpellint.core.validators.rules;

import api.SimpleValidationResult;
import bpellint.core.model.ProcessContainer;
import bpellint.core.model.bpel.EventHandlersElement;

public class SA00083Validator extends Validator {

    public SA00083Validator(ProcessContainer files,
                            SimpleValidationResult validationCollector) {
        super(files, validationCollector);
    }

    @Override
    public void validate() {
        for (EventHandlersElement faultHandler : processContainer.getAllEventHandlers()) {
            if (!faultHandler.hasOnEvents() && !faultHandler.hasOnAlarms()) {
                addViolation(faultHandler);
            }
        }
    }

    @Override
    public int getSaNumber() {
        return 83;
    }
}
