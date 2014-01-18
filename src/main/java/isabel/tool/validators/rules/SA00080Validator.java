package isabel.tool.validators.rules;

import isabel.model.ProcessContainer;
import isabel.model.bpel.fct.FaultHandlersElement;
import isabel.tool.impl.ValidationCollector;

public class SA00080Validator extends Validator {

    public SA00080Validator(ProcessContainer files,
                            ValidationCollector validationCollector) {
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
