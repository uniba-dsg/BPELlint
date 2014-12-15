package bpellint.core.validators.rules;


import java.util.HashSet;
import java.util.Set;

import api.SimpleValidationResult;
import bpellint.core.model.ProcessContainer;
import bpellint.core.model.bpel.CorrelationSetElement;
import bpellint.core.model.bpel.CorrelationSetsElement;

public class SA00044Validator extends Validator {

    public SA00044Validator(ProcessContainer files,
                            SimpleValidationResult violationCollector) {
        super(files, violationCollector);
    }

    @Override
    public void validate() {
        for (CorrelationSetsElement correlationSetContainer : processContainer.getAllCorrelationSetsContainer()) {
            Set<String> names = new HashSet<>();
            for (CorrelationSetElement correlationSet : correlationSetContainer.getCorrelationSets()) {
                String name = correlationSet.getName();

                if (names.contains(name)) {
                    addViolation(correlationSet);
                } else {
                    names.add(name);
                }
            }
        }
    }

    @Override
    public int getSaNumber() {
        return 44;
    }
}
