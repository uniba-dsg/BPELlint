package isabel.tool.validators.rules;

import isabel.model.ProcessContainer;
import isabel.model.bpel.var.VariableElement;
import isabel.model.bpel.var.VariablesElement;
import isabel.tool.validators.result.ValidationCollector;

import java.util.HashSet;
import java.util.Set;

public class SA00023Validator extends Validator {

    public SA00023Validator(ProcessContainer files,
                            ValidationCollector violationCollector) {
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
