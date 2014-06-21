package bpellint.tool.validators.rules;

import bpellint.model.ProcessContainer;
import bpellint.model.bpel.PartnerLinkElement;
import bpellint.tool.validators.result.ValidationCollector;

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
