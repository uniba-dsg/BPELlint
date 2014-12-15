package bpellint.core.model.bpel.var;

import static bpellint.core.model.Standards.CONTEXT;

import java.util.LinkedList;
import java.util.List;

import bpellint.core.model.ContainerAwareReferable;
import bpellint.core.model.NodeHelper;
import bpellint.core.model.ProcessContainer;

import nu.xom.Node;

public class FromPartsElement extends ContainerAwareReferable {

	public FromPartsElement(Node fromParts, ProcessContainer processContainer) {
		super(fromParts, processContainer);
		new NodeHelper(fromParts, "fromParts");
	}

	public List<FromPartElement> getAllFromParts() {
		List<FromPartElement> result = new LinkedList<>();

		for (Node node : toXOM().query(".//bpel:fromPart", CONTEXT)) {
			result.add(new FromPartElement(node, getProcessContainer()));
		}

		return result;
	}

}
