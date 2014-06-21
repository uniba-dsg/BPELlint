package bpellint.tool.validators.rules;

import bpellint.model.NavigationException;
import bpellint.model.PrefixHelper;
import bpellint.model.ProcessContainer;
import bpellint.model.bpel.mex.MessageActivity;
import bpellint.model.wsdl.PortTypeElement;
import bpellint.tool.validators.result.ValidationCollector;

public class SA00005Validator extends Validator {

    public SA00005Validator(ProcessContainer files,
                            ValidationCollector violationCollector) {
        super(files, violationCollector);
    }

    @Override
    public void validate() {
        for (MessageActivity messageActivity : processContainer.getMessageActivities()) {
            try {
                PortTypeElement portType = messageActivity.getPortType();

                String localPortTypeDefinition = PrefixHelper.removePrefix(messageActivity.getPortTypeAttribute());

                if (!localPortTypeDefinition.isEmpty() && !portType.getName().equals(localPortTypeDefinition)) {
                    addViolation(messageActivity);
                }
            } catch (NavigationException e) {
                // This node could not be validated
            }
        }
    }

    @Override
    public int getSaNumber() {
        return 5;
    }
}
