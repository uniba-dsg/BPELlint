package bpellint.core.model.bpel.var;

import static bpellint.core.model.Standards.CONTEXT;

import java.util.LinkedList;
import java.util.List;

import bpellint.core.model.ContainerAwareReferable;
import bpellint.core.model.NodeHelper;
import bpellint.core.model.ProcessContainer;

import nu.xom.Node;

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
