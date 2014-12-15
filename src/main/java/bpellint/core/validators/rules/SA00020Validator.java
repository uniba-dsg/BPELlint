package bpellint.core.validators.rules;

import api.SimpleValidationResult;
import bpellint.core.model.ProcessContainer;
import bpellint.core.model.wsdl.PropertyAliasElement;

public class SA00020Validator extends Validator {

    public SA00020Validator(ProcessContainer files,
                            SimpleValidationResult violationCollector) {
        super(files, violationCollector);
    }

    @Override
    public void validate() {
        for (PropertyAliasElement propertyAlias : processContainer.getAllPropertyAliases()) {
            boolean messageTypeAndPart = propertyAlias.hasMessageType()
                    && propertyAlias.hasPart()
                    && !propertyAlias.hasType()
                    && !propertyAlias.hasElement();
            boolean type = !propertyAlias.hasMessageType()
                    && !propertyAlias.hasPart()
                    && propertyAlias.hasType()
                    && !propertyAlias.hasElement();
            boolean element = !propertyAlias.hasMessageType()
                    && !propertyAlias.hasPart()
                    && !propertyAlias.hasType()
                    && propertyAlias.hasElement();
            if (!(messageTypeAndPart || type || element)) {
                addViolation(propertyAlias);
            }
        }
    }

    @Override
    public int getSaNumber() {
        return 20;
    }
}
