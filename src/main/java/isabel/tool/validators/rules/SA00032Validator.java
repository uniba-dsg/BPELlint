package isabel.tool.validators.rules;

import isabel.model.ProcessContainer;
import isabel.model.bpel.var.CopyEntity;
import isabel.tool.impl.ValidationCollector;

public class SA00032Validator extends Validator {

    public SA00032Validator(ProcessContainer files, ValidationCollector validationCollector) {
        super(files, validationCollector);
    }

    @Override
    public void validate() {
        for (CopyEntity fromTo : fileHandler.getAllCopyEntities()) {
		    if (!(fromTo.isEmpty() || fromTo.isMessageVariableAssignment()
		            || fromTo.isPartnerLinkAssignment() || fromTo.isVariableAssignment()
		            || fromTo.isQueryResultAssignment() || fromTo.isLiteralAssignment())) {
		        addViolation(fromTo);
		    }
		}
    }

    @Override
    public int getSaNumber() {
        return 32;
    }

}
