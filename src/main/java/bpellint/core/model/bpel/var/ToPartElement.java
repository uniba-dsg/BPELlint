package bpellint.core.model.bpel.var;

import bpellint.core.model.ContainerAwareReferable;
import bpellint.core.model.NodeHelper;
import bpellint.core.model.ProcessContainer;
import nu.xom.Node;

public class ToPartElement extends ContainerAwareReferable {

	private NodeHelper toPart;

	public ToPartElement(Node toPart, ProcessContainer processContainer) {
		super(toPart, processContainer);
		this.toPart = new NodeHelper(toPart, "toPart");
	}

	public String getPartAttribute() {
		return toPart.getAttribute("part");
	}

	public String getFromVariable() {
		return toPart.getAttribute("fromVariable");
	}

}
