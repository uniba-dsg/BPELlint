package bpellint.model.bpel.var;

import bpellint.model.ContainerAwareReferable;
import bpellint.model.NodeHelper;
import bpellint.model.ProcessContainer;
import nu.xom.Node;

public class FromPartElement extends ContainerAwareReferable {

	private final NodeHelper fromPart;

	public FromPartElement(Node fromPart, ProcessContainer processContainer) {
		super(fromPart, processContainer);
		this.fromPart = new NodeHelper(fromPart, "fromPart");
	}

	public String getToVariable() {
		return fromPart.getAttribute("toVariable");
	}

	public String getPartAttribute() {
		return fromPart.getAttribute("part");
	}
}
