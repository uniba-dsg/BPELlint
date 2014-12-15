package bpellint.core.validators.rules;


import java.util.HashSet;
import java.util.Set;

import api.SimpleValidationResult;
import bpellint.core.model.ProcessContainer;
import bpellint.core.model.bpel.flow.SourceElement;
import bpellint.core.model.bpel.flow.SourcesElement;

public class SA00068Validator extends Validator {

    public SA00068Validator(ProcessContainer files,
                            SimpleValidationResult validationCollector) {
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
