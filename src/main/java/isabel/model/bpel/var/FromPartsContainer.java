package isabel.model.bpel.var;

import static isabel.model.Standards.CONTEXT;

import java.util.LinkedList;
import java.util.List;

import nu.xom.Node;
import isabel.model.NodeHelper;
import isabel.model.Referable;

public class FromPartsContainer implements Referable {

	private NodeHelper fromParts;
	
	public FromPartsContainer(Node node) {
		fromParts = new NodeHelper(node,"fromParts");
	}
	
    public List<FromPartElement> getAllFromParts() {
        List<FromPartElement> result = new LinkedList<>();

        for (Node node : toXOM().query("//bpel:fromPart", CONTEXT)) {
            result.add(new FromPartElement(node));
        }

        return result;
    }
    
	@Override
	public Node toXOM() {
		return fromParts.toXOM();
	}

}
