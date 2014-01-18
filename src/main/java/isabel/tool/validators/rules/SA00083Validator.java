package isabel.tool.validators.rules;

import isabel.model.ProcessContainer;
import isabel.model.bpel.EventHandlersElement;
import isabel.tool.impl.ValidationCollector;

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
