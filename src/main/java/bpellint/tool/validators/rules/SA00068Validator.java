package bpellint.tool.validators.rules;


import java.util.HashSet;
import java.util.Set;

import bpellint.model.ProcessContainer;
import bpellint.model.bpel.flow.SourceElement;
import bpellint.model.bpel.flow.SourcesElement;
import bpellint.tool.validators.result.ValidationCollector;

public class SA00068Validator extends Validator {

    public SA00068Validator(ProcessContainer files,
                            ValidationCollector validationCollector) {
        super(files, validationCollector);
    }

    @Override
    public void validate() {
        for (SourcesElement sources : processContainer.getAllSourcesContainer()) {
            checkLinkNameUniqueness(sources);
        }
    }

    private void checkLinkNameUniqueness(SourcesElement sourcesElement) {
        Set<String> uniqueNames = new HashSet<>();
        for (SourceElement source : sourcesElement.getAllSources()) {
            String name = source.getLinkName();

            if (uniqueNames.contains(name)) {
                addViolation(source);
            } else {
                uniqueNames.add(name);
            }
        }
    }

    @Override
    public int getSaNumber() {
        return 68;
    }

}
