package bpellint.tool.validators.rules;

import bpellint.model.ProcessContainer;
import bpellint.model.bpel.var.CopyEntity;
import bpellint.tool.validators.result.ValidationCollector;

public class SA00032Validator extends Validator {

    public SA00032Validator(ProcessContainer files, ValidationCollector validationCollector) {
        super(files, validationCollector);
    }

    @Override
    public void validate() {
        processContainer.getAllCopyEntities().forEach(fromTo -> validate(fromTo));
    }

    private void validate(CopyEntity fromTo) {
        if (!(fromTo.isEmpty() || fromTo.isMessageVariableAssignment()
                || fromTo.isPartnerLinkAssignment() || fromTo.isVariableAssignment()
                || fromTo.isQueryResultAssignment() || fromTo.isLiteralAssignment())) {
            addViolation(fromTo);
        }
    }

    @Override
    public int getSaNumber() {
        return 32;
    }

}
