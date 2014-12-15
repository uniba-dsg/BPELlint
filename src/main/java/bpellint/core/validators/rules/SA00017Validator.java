package bpellint.core.validators.rules;

import api.SimpleValidationResult;
import bpellint.core.model.ProcessContainer;
import bpellint.core.model.bpel.PartnerLinkElement;

public class SA00017Validator extends Validator {

    public SA00017Validator(ProcessContainer files, SimpleValidationResult violationCollector) {
        super(files, violationCollector);
    }

    @Override
    public void validate() {
        for (PartnerLinkElement partnerLink : processContainer.getAllPartnerLinks()) {
            if (partnerLink.hasInitializePartnerRole() && !partnerLink.hasPartnerRole()) {
                addViolation(partnerLink);
            }
        }
    }

    @Override
    public int getSaNumber() {
        return 17;
    }
}
