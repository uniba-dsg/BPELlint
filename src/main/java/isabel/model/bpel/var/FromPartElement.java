package isabel.model.bpel.var;

import isabel.model.ContainerAwareReferable;
import isabel.model.NodeHelper;
import isabel.model.ProcessContainer;
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

}
