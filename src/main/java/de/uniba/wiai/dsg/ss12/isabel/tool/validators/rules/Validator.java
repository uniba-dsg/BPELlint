package de.uniba.wiai.dsg.ss12.isabel.tool.validators.rules;

import de.uniba.wiai.dsg.ss12.isabel.tool.ValidationResult;
import de.uniba.wiai.dsg.ss12.isabel.tool.Violation;
import de.uniba.wiai.dsg.ss12.isabel.tool.helper.NodeHelper;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.BpelProcessFiles;
import nu.xom.Attribute;
import nu.xom.Element;
import nu.xom.Node;

public abstract class Validator {

	private static final int DEFAULT_TYPE = 1;

	protected final BpelProcessFiles fileHandler;
	protected final ValidatorNavigator navigator;
	private final ValidationResult validationResult;

	public Validator(BpelProcessFiles files, ValidationResult validationResult) {
		this.fileHandler = files;
		this.validationResult = validationResult;
		navigator = new ValidatorNavigator(fileHandler);
	}

	public abstract void validate();

	public abstract int getSaNumber();

	private void addViolation(String fileName, Node node, int type) {
		validationResult.add(new Violation(fileName, getSaNumber(), type,
                getLineNumber(node), getColumnNumber(node)));
	}

	protected void addViolation(Node node) {
		addViolation(node, DEFAULT_TYPE);
	}
	
	protected void addViolation(Node node, int type) {
		addViolation(new NodeHelper(node).getFilePath(), node, type);
	}

	private int getLineNumber(Node node) {
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

	private int getColumnNumber(Node node) {
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

	protected Element toElement(Node element) {
		if (!(element instanceof Element)) {
			throw new IllegalArgumentException(
					"Given Node must not be null or an attribute.");
		}

		return (Element) element;
	}

	public String getBpelFileName() {
		return fileHandler.getBpel().getFilePath();
	}

}
