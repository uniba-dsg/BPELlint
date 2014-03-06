package isabel.tool.validators.rules;

import isabel.model.ProcessContainer;
import isabel.model.bpel.flow.SourceElement;
import isabel.model.bpel.flow.SourcesElement;
import isabel.tool.validators.result.ValidationCollector;

import java.util.HashSet;
import java.util.Set;

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
