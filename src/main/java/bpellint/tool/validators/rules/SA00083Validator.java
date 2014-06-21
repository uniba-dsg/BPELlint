package bpellint.tool.validators.rules;

import bpellint.model.ProcessContainer;
import bpellint.model.bpel.EventHandlersElement;
import bpellint.tool.validators.result.ValidationCollector;

public class SA00083Validator extends Validator {

    public SA00083Validator(ProcessContainer files,
                            ValidationCollector validationCollector) {
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
