package bpellint.core.validators.rules;


import java.util.HashSet;
import java.util.Set;

import api.SimpleValidationResult;
import bpellint.core.model.ProcessContainer;
import bpellint.core.model.bpel.PartnerLinkElement;
import bpellint.core.model.bpel.PartnerLinksElement;

public class SA00018Validator extends Validator {

    public SA00018Validator(ProcessContainer files, SimpleValidationResult validationCollector) {
        super(files, validationCollector);
    }

    @Override
    public void validate() {
        for (PartnerLinksElement partnerLinksContainer : processContainer.getAllPartnerLinksContainer()) {
            Set<String> uniqueNames = new HashSet<>();
            for (PartnerLinkElement partnerLink : partnerLinksContainer.getPartnerLinks()) {
                String name = partnerLink.getName();
                if (uniqueNames.contains(name)) {
                    addViolation(partnerLink);
                } else {
                    uniqueNames.add(name);
                }
            }
        }
    }

    @Override
    public int getSaNumber() {
        return 18;
    }
}
