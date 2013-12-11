package isabel.model.bpel;

import isabel.model.NodeHelper;
import nu.xom.Node;

import java.util.LinkedList;
import java.util.List;

import static isabel.model.Standards.CONTEXT;

public class CorrelationSetsElement extends NodeHelper {

    public CorrelationSetsElement(Node node) {
        super(node);
    }

    public List<CorrelationSetElement> getCorrelationSets() {

        List<CorrelationSetElement> result = new LinkedList<>();

        for (Node node : toXOM().query("bpel:correlationSet", CONTEXT)) {
            result.add(new CorrelationSetElement(node));
        }

        return result;
    }
}
