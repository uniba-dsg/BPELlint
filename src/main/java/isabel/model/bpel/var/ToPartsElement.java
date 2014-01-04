package isabel.model.bpel.var;

import static isabel.model.Standards.CONTEXT;

import java.util.LinkedList;
import java.util.List;

import nu.xom.Node;
import isabel.model.ContainerAwareReferable;
import isabel.model.NodeHelper;
import isabel.model.ProcessContainer;

public class ToPartsElement extends ContainerAwareReferable {

	public ToPartsElement(Node toParts, ProcessContainer processContainer) {
		super(toParts, processContainer);
		new NodeHelper(toParts, "toParts");
	}

	public List<ToPartElement> getAllToParts() {
		List<ToPartElement> result = new LinkedList<>();
		for (Node node : toXOM().query(".//bpel:toPart", CONTEXT)) {
			result.add(new ToPartElement(node, getProcessContainer()));
		}
		return result;
	}

}
