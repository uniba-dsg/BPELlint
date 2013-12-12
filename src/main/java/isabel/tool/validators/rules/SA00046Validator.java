package isabel.tool.validators.rules;

import isabel.model.NavigationException;
import isabel.model.ProcessContainer;
import isabel.model.bpel.CorrelationElement;
import isabel.model.bpel.CorrelationsElement;
import isabel.model.bpel.mex.InvokeElement;
import isabel.model.wsdl.OperationElement;
import isabel.tool.impl.ValidationCollector;

import java.util.List;

public class SA00046Validator extends Validator {
    public SA00046Validator(ProcessContainer files,
                            ValidationCollector violationCollector) {
        super(files, violationCollector);
    }

    @Override
    public void validate() {
        for (CorrelationsElement node : fileHandler.getAllCorrelationsWithinInvokes()) {
            try {
                OperationElement operation = new InvokeElement(node.toXOM().getParent(), fileHandler).getOperation();

                if (operation.isRequestResponse()) {
                    reportViolation(node.getCorrelationWithoutPattern(), 1);
                } else if (operation.isOneWay()) {
                    reportViolation(node.getCorrelationWithPattern(), 2);
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
