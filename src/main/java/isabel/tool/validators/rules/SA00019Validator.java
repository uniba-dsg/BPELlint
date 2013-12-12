package isabel.tool.validators.rules;

import isabel.model.wsdl.PropertyElement;
import isabel.tool.impl.ValidationCollector;
import isabel.model.ProcessContainer;
import isabel.model.XmlFile;

public class SA00019Validator extends Validator {

    public static final int NEITHER_TYPE_NOR_ELEMENT = 1;
    public static final int TYPE_AND_ELEMENT = 2;

    public SA00019Validator(ProcessContainer files,
                            ValidationCollector validationCollector) {
        super(files, validationCollector);
    }

    @Override
    public void validate() {
        for (PropertyElement property : fileHandler.getAllProperties()) {
            if (property.hasNeitherTypeNorElement()) {
                addViolation(property, NEITHER_TYPE_NOR_ELEMENT);
            } else if (property.hasTypeAndElement()) {
                addViolation(property, TYPE_AND_ELEMENT);
            }
        }
    }

    @Override
    public int getSaNumber() {
        return 19;
    }
}
