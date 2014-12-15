package bpellint.core.validators.rules;


import java.util.HashSet;
import java.util.Set;

import api.SimpleValidationResult;
import bpellint.core.model.ProcessContainer;
import bpellint.core.model.bpel.var.VariableElement;
import bpellint.core.model.bpel.var.VariablesElement;

public class SA00023Validator extends Validator {

    public SA00023Validator(ProcessContainer files,
                            SimpleValidationResult violationCollector) {
        super(files, violationCollector);
    }

    @Override
    public void validate() {
        for (VariablesElement variablesContainer : processContainer.getAllVariablesContainer()) {
            Set<String> names = new HashSet<>();
            for (VariableElement variable : variablesContainer.getVariables()) {
                String name = variable.getVariableName();
                if (names.contains(name)) {
                    addViolation(variable);
                } else {
                    names.add(name);
                }
            }
        }
    }

    @Override
    public int getSaNumber() {
        return 23;
    }

}
