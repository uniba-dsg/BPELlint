package bpellint.model.wsdl;

import bpellint.model.ContainerAwareReferable;
import bpellint.model.NodeHelper;
import bpellint.model.ProcessContainer;
import nu.xom.Node;

public class PartElement extends ContainerAwareReferable {

	private final NodeHelper part;

	public PartElement(Node part, ProcessContainer processContainer) {
		super(part, processContainer);
		this.part = new NodeHelper(part, "part");
	}

	public String getElement() {
		return part.getAttribute("element");
	}

	public String getNameAttribute() {
		return part.getAttribute("name");
	}

	public boolean hasTypeAttribute() {
		return part.hasAttribute("type");
	}

	public String getTypeAttribute() {
		return part.getAttribute("type");
	}

	public boolean hasElement() {
		return part.hasAttribute("element");
	}
}
