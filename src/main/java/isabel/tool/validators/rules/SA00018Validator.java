package isabel.tool.validators.rules;

import isabel.model.ProcessContainer;
import isabel.model.bpel.PartnerLinkElement;
import isabel.model.bpel.PartnerLinksElement;
import isabel.tool.impl.ValidationCollector;

import java.util.HashSet;
import java.util.Set;

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
