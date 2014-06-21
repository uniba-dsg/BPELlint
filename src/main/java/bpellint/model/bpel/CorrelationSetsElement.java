package bpellint.model.bpel;

import nu.xom.Node;

import java.util.LinkedList;
import java.util.List;

import bpellint.model.ContainerAwareReferable;
import bpellint.model.ProcessContainer;

import static bpellint.model.Standards.CONTEXT;

public class CorrelationSetsElement extends ContainerAwareReferable {

    public CorrelationSetsElement(Node node, ProcessContainer processContainer) {
        super(node, processContainer);
    }

    public List<CorrelationSetElement> getCorrelationSets() {
        List<CorrelationSetElement> result = new LinkedList<>();

        for (Node node : toXOM().query("bpel:correlationSet", CONTEXT)) {
            result.add(new CorrelationSetElement(node, getProcessContainer()));
        }

        return result;
    }
}
