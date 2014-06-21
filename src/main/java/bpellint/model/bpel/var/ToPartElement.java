package bpellint.model.bpel.var;

import bpellint.model.ContainerAwareReferable;
import bpellint.model.NodeHelper;
import bpellint.model.ProcessContainer;
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
