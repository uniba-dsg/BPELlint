package bpellint.model.bpel;

import nu.xom.Node;

import java.util.LinkedList;
import java.util.List;

import bpellint.model.ContainerAwareReferable;
import bpellint.model.ProcessContainer;
import bpellint.model.Standards;

public class CorrelationsElement extends ContainerAwareReferable {

    public CorrelationsElement(Node node, ProcessContainer processContainer) {
        super(node, processContainer);
    }

    public List<CorrelationElement> getCorrelationWithoutPattern() {
        List<CorrelationElement> result = new LinkedList<>();

        for (Node node : toXOM().query("child::bpel:correlation[not(attribute::pattern)]", Standards.CONTEXT)) {
            result.add(new CorrelationElement(node, getProcessContainer()));
        }

        return result;
    }

    public List<CorrelationElement> getCorrelationWithPattern() {
        List<CorrelationElement> result = new LinkedList<>();

        for (Node node : toXOM().query("child::bpel:correlation[attribute::pattern]", Standards.CONTEXT)) {
            result.add(new CorrelationElement(node, getProcessContainer()));
        }

        return result;
    }

    public List<CorrelationElement> getCorrelations() {
        List<CorrelationElement> result = new LinkedList<>();

        for (Node node : toXOM().query("./bpel:correlation", Standards.CONTEXT)) {
            result.add(new CorrelationElement(node, getProcessContainer()));
        }

        return result;
    }
}
