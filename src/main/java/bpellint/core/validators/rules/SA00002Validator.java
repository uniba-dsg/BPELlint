package bpellint.core.validators.rules;

import api.SimpleValidationResult;
import nu.xom.Attribute;

import java.util.HashSet;
import java.util.Set;

import bpellint.core.model.ProcessContainer;
import bpellint.core.model.wsdl.PortTypeElement;

public class SA00002Validator extends Validator {

    public SA00002Validator(ProcessContainer files, SimpleValidationResult violationCollector) {
        super(files, violationCollector);
    }

    @Override
    public void validate() {
        for (PortTypeElement portType : processContainer.getAllPortTypes()) {
            Set<String> nameSet = new HashSet<>();
            for (Attribute nameAttribute : portType.getOperationNames()) {
                if (nameSet.contains(nameAttribute.toXML())) {
                    addViolation(nameAttribute);
                } else {
                    nameSet.add(nameAttribute.toXML());
                }
            }
        }
    }

    @Override
    public int getSaNumber() {
        return 2;
    }
}
