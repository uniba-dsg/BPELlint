package isabel.model.bpel;

import isabel.model.NodeHelper;
import isabel.model.Standards;
import nu.xom.Node;

import java.util.LinkedList;
import java.util.List;

public class CorrelationsElement extends NodeHelper {

    public CorrelationsElement(Node node) {
        super(node);
    }

    public List<CorrelationElement> getCorrelationWithoutPattern() {
        List<CorrelationElement> result = new LinkedList<>();

        for (Node node : toXOM().query("child::bpel:correlation[not(attribute::pattern)]", Standards.CONTEXT)) {
            result.add(new CorrelationElement(node));
        }

        return result;
    }

    public List<CorrelationElement> getCorrelationWithPattern() {
        List<CorrelationElement> result = new LinkedList<>();

        for (Node node : toXOM().query("child::bpel:correlation[attribute::pattern]", Standards.CONTEXT)) {
            result.add(new CorrelationElement(node));
        }

        return result;
    }
    
    public List<CorrelationElement> getCorrelations() {
        List<CorrelationElement> result = new LinkedList<>();

        for (Node node : toXOM().query("./bpel:correlation", Standards.CONTEXT)) {
            result.add(new CorrelationElement(node));
        }

        return result;
    }
}
