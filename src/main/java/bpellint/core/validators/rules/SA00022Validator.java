package bpellint.core.validators.rules;


import java.util.List;

import api.SimpleValidationResult;
import bpellint.core.model.ProcessContainer;
import bpellint.core.model.wsdl.PropertyAliasElement;

public class SA00022Validator extends Validator {

    private static final int SAME_MESSAGE_TYPE = 3;
    private static final int SAME_ELEMENT = 2;
    private static final int SAME_TYPE = 1;

    public SA00022Validator(ProcessContainer files,
                            SimpleValidationResult violationCollector) {
        super(files, violationCollector);
    }

    @Override
    public void validate() {
        List<PropertyAliasElement> allPropertyAliases = processContainer.getAllPropertyAliases();
        for (int i = 0, allPropertyAliasesSize = allPropertyAliases.size(); i < allPropertyAliasesSize; i++) {

            PropertyAliasElement propertyAlias = allPropertyAliases.get(i);

            for (int j = i + 1; j < allPropertyAliasesSize; j++) {

                PropertyAliasElement otherPropertyAlias = allPropertyAliases.get(j);

                if (!propertyAlias.hasSamePropertyName(otherPropertyAlias)) {
                    continue;// go to next element
                }

                if (propertyAlias.hasSameType(otherPropertyAlias)) {
                    addViolation(propertyAlias, SAME_TYPE);
                }
                if (propertyAlias.hasSameElement(otherPropertyAlias)) {
                    addViolation(propertyAlias, SAME_ELEMENT);
                }
                if (propertyAlias.hasSameMessageType(otherPropertyAlias)) {
                    addViolation(propertyAlias, SAME_MESSAGE_TYPE);
                }
            }
        }
    }

    @Override
    public int getSaNumber() {
        return 22;
    }
}