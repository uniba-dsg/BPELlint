package bpellint.model.bpel.var;

import static bpellint.model.Standards.CONTEXT;

import java.util.LinkedList;
import java.util.List;

import bpellint.model.ContainerAwareReferable;
import bpellint.model.NodeHelper;
import bpellint.model.ProcessContainer;

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
