package bpellint.tool.validators.rules;

import bpellint.model.Navigator;
import bpellint.model.NodeHelper;
import bpellint.model.NodeToId;
import bpellint.model.ProcessContainer;
import bpellint.model.Referable;
import bpellint.tool.validators.result.ValidationCollector;
import bpellint.tool.validators.result.Violation;
import nu.xom.Node;

public abstract class Validator {

	private static final int DEFAULT_TYPE = 1;

	protected final ProcessContainer processContainer;
	protected final Navigator navigator;
	private final ValidationCollector validationCollector;

	public Validator(ProcessContainer processContainer,
	                 ValidationCollector validationCollector) {
		this.processContainer = processContainer;
		this.validationCollector = validationCollector;
		navigator = new Navigator(processContainer);
	}

	public abstract void validate();

	public abstract int getSaNumber();

	private void addViolation(String fileName, Node node, int type) {
		NodeToId identifiableNode = new NodeToId(node);
		validationCollector.add(new Violation(fileName, getSaNumber(), type,
				identifiableNode.getLineNumber(), identifiableNode.getColumnNumber()));
	}

    protected void addViolation(Referable node) {
        addViolation(node.toXOM(), DEFAULT_TYPE);
    }

    protected void addViolation(Referable node, int type) {
        addViolation(node.toXOM(), type);
    }

	protected void addViolation(Node node) {
		addViolation(node, DEFAULT_TYPE);
	}

	protected void addViolation(Node node, int type) {
		addViolation(new NodeHelper(node).getFilePath(), node, type);
	}

	public String getBpelFileName() {
		return processContainer.getBpel().getFilePath().getFileName().toString();
	}

}
