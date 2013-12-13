package isabel.tool.validators.rules;

import isabel.model.wsdl.PortTypeElement;
import isabel.tool.impl.ValidationCollector;
import isabel.model.ProcessContainer;
import nu.xom.Attribute;

import java.util.HashSet;
import java.util.Set;

public class SA00002Validator extends Validator {

    public SA00002Validator(ProcessContainer files, ValidationCollector violationCollector) {
        super(files, violationCollector);
    }

    @Override
    public void validate() {
        for (PortTypeElement portType : fileHandler.getAllPortTypes()) {
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
