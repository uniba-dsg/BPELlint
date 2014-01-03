package isabel.model.bpel.var;

import static isabel.model.Standards.CONTEXT;

import java.util.LinkedList;
import java.util.List;

import nu.xom.Node;
import isabel.model.ContainerAwareReferable;
import isabel.model.NodeHelper;
import isabel.model.ProcessContainer;

public class FromPartsContainer extends ContainerAwareReferable {

	public FromPartsContainer(Node fromParts, ProcessContainer processContainer) {
		super(fromParts, processContainer);
		new NodeHelper(fromParts,"fromParts");
	}
	
    public List<FromPartElement> getAllFromParts() {
        List<FromPartElement> result = new LinkedList<>();

        for (Node node : toXOM().query(".//bpel:fromPart", CONTEXT)) {
            result.add(new FromPartElement(node, getProcessContainer()));
        }

        return result;
    }

}
