package isabel.tool.validators.rules;

import isabel.model.ProcessContainer;
import isabel.model.bpel.mex.InvokeElement;
import isabel.tool.impl.ValidationCollector;

public class SA00052Validator extends Validator {
    public SA00052Validator(ProcessContainer files,
                            ValidationCollector violationCollector) {
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
