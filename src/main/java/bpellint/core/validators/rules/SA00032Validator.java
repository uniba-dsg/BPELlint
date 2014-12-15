package bpellint.core.validators.rules;

import api.SimpleValidationResult;
import bpellint.core.model.ProcessContainer;
import bpellint.core.model.bpel.var.CopyEntity;

public class SA00032Validator extends Validator {

    public SA00032Validator(ProcessContainer files, SimpleValidationResult validationCollector) {
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
