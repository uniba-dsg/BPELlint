package bpellint.core.model.bpel.var;

import bpellint.core.model.ContainerAwareReferable;
import bpellint.core.model.NodeHelper;
import bpellint.core.model.ProcessContainer;
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
