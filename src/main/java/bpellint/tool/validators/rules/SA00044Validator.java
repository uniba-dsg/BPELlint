package bpellint.tool.validators.rules;


import java.util.HashSet;
import java.util.Set;

import bpellint.model.ProcessContainer;
import bpellint.model.bpel.CorrelationSetElement;
import bpellint.model.bpel.CorrelationSetsElement;
import bpellint.tool.validators.result.ValidationCollector;

public class SA00044Validator extends Validator {

    public SA00044Validator(ProcessContainer files,
                            ValidationCollector violationCollector) {
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
