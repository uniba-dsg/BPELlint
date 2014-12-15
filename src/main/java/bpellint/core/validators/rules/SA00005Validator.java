package bpellint.core.validators.rules;

import api.SimpleValidationResult;
import bpellint.core.model.NavigationException;
import bpellint.core.model.PrefixHelper;
import bpellint.core.model.ProcessContainer;
import bpellint.core.model.bpel.mex.MessageActivity;
import bpellint.core.model.wsdl.PortTypeElement;

public class SA00005Validator extends Validator {

    public SA00005Validator(ProcessContainer files,
                            SimpleValidationResult violationCollector) {
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
