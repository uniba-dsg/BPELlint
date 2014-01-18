package isabel.tool.validators.rules;

import isabel.model.NodeToId;
import isabel.model.Referable;
import isabel.tool.Violation;
import isabel.model.NodeHelper;
import isabel.tool.impl.ValidationCollector;
import isabel.model.ProcessContainer;
import nu.xom.Node;

public abstract class Validator {

	private static final int DEFAULT_TYPE = 1;

	protected final ProcessContainer processContainer;
	protected final ValidatorNavigator navigator;
	private final ValidationCollector validationCollector;

	public Validator(ProcessContainer processContainer,
	                 ValidationCollector validationCollector) {
		this.processContainer = processContainer;
		this.validationCollector = validationCollector;
		navigator = new ValidatorNavigator(processContainer);
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
