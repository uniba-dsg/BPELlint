package bpellint.tool.validators.rules;


import java.util.List;

import bpellint.model.NavigationException;
import bpellint.model.ProcessContainer;
import bpellint.model.bpel.CorrelationElement;
import bpellint.model.bpel.CorrelationsElement;
import bpellint.model.bpel.mex.InvokeElement;
import bpellint.model.wsdl.OperationElement;
import bpellint.tool.validators.result.ValidationCollector;

public class SA00046Validator extends Validator {

    public static final int PATTERN_REQUIRED_IN_REQUEST_RESPONSE_INVOKE = 1;
    public static final int PATTERN_NOT_ALLOWED_IN_ONE_WAY_INVOKE = 2;

    public SA00046Validator(ProcessContainer files, ValidationCollector violationCollector) {
        super(files, violationCollector);
    }

    @Override
    public void validate() {
        for (CorrelationsElement node : processContainer.getAllCorrelationsWithinInvokes()) {
            try {
                OperationElement operation = new InvokeElement(node.toXOM().getParent(), processContainer).getOperation();

                if (operation.isRequestResponse()) {
                    reportViolation(node.getCorrelationWithoutPattern(), PATTERN_REQUIRED_IN_REQUEST_RESPONSE_INVOKE);
                } else if (operation.isOneWay()) {
                    reportViolation(node.getCorrelationWithPattern(), PATTERN_NOT_ALLOWED_IN_ONE_WAY_INVOKE);
                }
            } catch (NavigationException e) {
                // This node could not be validated
            }

        }
    }

    private void reportViolation(List<CorrelationElement> correlations, int type) {
        for (CorrelationElement correlation : correlations) {
            addViolation(correlation, type);
        }
    }

    @Override
    public int getSaNumber() {
        return 46;
    }

}
