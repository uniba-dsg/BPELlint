package bpellint.tool.validators.rules;


import java.util.HashSet;
import java.util.Set;

import bpellint.model.ProcessContainer;
import bpellint.model.bpel.PartnerLinkElement;
import bpellint.model.bpel.PartnerLinksElement;
import bpellint.tool.validators.result.ValidationCollector;

public class SA00018Validator extends Validator {

    public SA00018Validator(ProcessContainer files, ValidationCollector validationCollector) {
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
