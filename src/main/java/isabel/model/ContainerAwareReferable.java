package isabel.model;

import nu.xom.Node;

public class ContainerAwareReferable implements Referable {
	
	private final Node node;
	private final ProcessContainer processContainer;
	
	public ContainerAwareReferable(Node node, ProcessContainer processContainer) {
		super();
		this.node = node;
		this.processContainer = processContainer;
	}
	
	public ProcessContainer getProcessContainer() {
		return processContainer;
	}
	
	@Override
	public Node toXOM() {
		return node;
	}

}
