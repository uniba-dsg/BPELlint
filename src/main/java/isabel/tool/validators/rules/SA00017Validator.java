package isabel.tool.validators.rules;

import isabel.model.ProcessContainer;
import isabel.model.bpel.PartnerLinkElement;
import isabel.tool.validators.result.ValidationCollector;

public class SA00017Validator extends Validator {

    public SA00017Validator(ProcessContainer files, ValidationCollector violationCollector) {
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
