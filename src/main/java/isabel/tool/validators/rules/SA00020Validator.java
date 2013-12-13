package isabel.tool.validators.rules;

import isabel.model.wsdl.PropertyAliasElement;
import isabel.tool.impl.ValidationCollector;
import isabel.model.ProcessContainer;

public class SA00020Validator extends Validator {

    public SA00020Validator(ProcessContainer files,
                            ValidationCollector violationCollector) {
        super(files, violationCollector);
    }

    @Override
    public void validate() {
        for (PropertyAliasElement propertyAlias : fileHandler.getAllPropertyAliases()) {
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
