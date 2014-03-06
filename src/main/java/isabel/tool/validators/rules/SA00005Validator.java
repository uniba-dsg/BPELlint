package isabel.tool.validators.rules;

import isabel.model.NavigationException;
import isabel.model.PrefixHelper;
import isabel.model.ProcessContainer;
import isabel.model.bpel.mex.MessageActivity;
import isabel.model.wsdl.PortTypeElement;
import isabel.tool.validators.result.ValidationCollector;

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
