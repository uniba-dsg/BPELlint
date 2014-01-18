package isabel.tool.validators.rules;

import isabel.model.ProcessContainer;
import isabel.model.bpel.PartnerLinkElement;
import isabel.tool.impl.ValidationCollector;

public class SA00016Validator extends Validator {

    public SA00016Validator(ProcessContainer files, ValidationCollector violationCollector) {
        super(files, violationCollector);
    }

    @Override
    public void validate() {
        for (PartnerLinkElement partnerLink : processContainer.getAllPartnerLinks()) {
            if (partnerLink.hasNeitherMyRoleNorPartnerRole()) {
                addViolation(partnerLink);
            }
        }
    }

    @Override
    public int getSaNumber() {
        return 16;
    }
}
