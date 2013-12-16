package isabel.tool.validators.rules;

import isabel.model.Referable;
import isabel.tool.Violation;
import isabel.model.NodeHelper;
import isabel.tool.impl.ValidationCollector;
import isabel.model.ProcessContainer;
import nu.xom.Attribute;
import nu.xom.Element;
import nu.xom.Node;

public abstract class Validator {

	private static final int DEFAULT_TYPE = 1;

	protected final ProcessContainer fileHandler;
	protected final ValidatorNavigator navigator;
	private final ValidationCollector validationCollector;

	public Validator(ProcessContainer files,
	                 ValidationCollector validationCollector) {
		this.fileHandler = files;
		this.validationCollector = validationCollector;
		navigator = new ValidatorNavigator();
	}

	public abstract void validate();

	public abstract int getSaNumber();

	private void addViolation(String fileName, Node node, int type) {
		validationCollector.add(new Violation(fileName, getSaNumber(), type,
				getLineNumber(node), getColumnNumber(node)));
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

	protected int getLineNumber(Node node) {
		if (node instanceof Element) {
			Element element = (Element) node;
			return (Integer) element.getUserData("lineNumber");
		} else if (node instanceof Attribute) {
			return getLineNumber(node.getParent());
		} else {
			throw new IllegalArgumentException(
					"Node need to be an Element or Attribute.");
		}
	}

	protected int getColumnNumber(Node node) {
		if (node instanceof Element) {
			Element element = (Element) node;
			return (Integer) element.getUserData("columnNumber");
		} else if (node instanceof Attribute) {
			return getColumnNumber(node.getParent());
		} else {
			throw new IllegalArgumentException(
					"Node need to be an Element or Attribute.");
		}
	}

	public String getBpelFileName() {
		return fileHandler.getBpel().getFilePath().getFileName().toString();
	}

}
