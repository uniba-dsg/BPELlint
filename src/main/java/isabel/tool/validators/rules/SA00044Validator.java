package isabel.tool.validators.rules;

import isabel.model.ProcessContainer;
import isabel.model.bpel.CorrelationSetElement;
import isabel.model.bpel.CorrelationSetsElement;
import isabel.tool.validators.result.ValidationCollector;

import java.util.HashSet;
import java.util.Set;

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
