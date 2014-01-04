package isabel.model.bpel.var;

import nu.xom.Node;
import isabel.model.ContainerAwareReferable;
import isabel.model.NodeHelper;
import isabel.model.ProcessContainer;

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
