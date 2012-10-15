package de.uniba.wiai.dsg.ss12.isabel.tool.validators;

import nu.xom.Attribute;
import nu.xom.Element;
import nu.xom.Node;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.BpelProcessFiles;
import de.uniba.wiai.dsg.ss12.isabel.tool.reports.Violation;
import de.uniba.wiai.dsg.ss12.isabel.tool.reports.ViolationCollector;

public abstract class Validator {

	protected final BpelProcessFiles fileHandler;
	private final ViolationCollector violationCollector;
	protected boolean valid = true;
	protected final ValidatorNavigator navigator;

	public Validator(BpelProcessFiles files,
			ViolationCollector violationCollector) {
		this.fileHandler = files;
		this.violationCollector = violationCollector;
		navigator = new ValidatorNavigator(fileHandler);
	}

	public abstract boolean validate();

	public abstract int getSaNumber();

	protected void addViolation(String fileName, Node node, int type) {
		valid = false;
		violationCollector.add(new Violation(fileName, getSaNumber(), type,
				getLineNumber(node), getColumnNumber(node)));
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
			return (Integer) element
					.getUserData("columnNumber");
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

	protected String getBpelFileName() {
		return fileHandler.getBpel().getFilePath();
	}

}
