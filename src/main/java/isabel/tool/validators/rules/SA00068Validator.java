package isabel.tool.validators.rules;

import isabel.model.ProcessContainer;
import isabel.model.Standards;
import isabel.model.bpel.SourceElement;
import isabel.model.bpel.SourcesElement;
import isabel.tool.impl.ValidationCollector;

import java.util.HashSet;
import java.util.Set;

import nu.xom.Node;
import nu.xom.Nodes;

public class SA00068Validator extends Validator {

    public SA00068Validator(ProcessContainer files,
                            ValidationCollector validationCollector) {
        super(files, validationCollector);
    }

    @Override
    public void validate() {
        Nodes sourcesNodes = this.fileHandler.getBpel().getDocument()
                .query("//bpel:sources", Standards.CONTEXT);
        for (Node sources : sourcesNodes) {
            checkLinkNameUniqueness(new SourcesElement(sources));
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
