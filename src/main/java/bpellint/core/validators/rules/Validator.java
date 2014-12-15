package bpellint.core.validators.rules;

import api.*;
import bpellint.core.validators.rules.infos.DescriptionNotFoundException;
import bpellint.core.validators.rules.infos.ErrorMessageRepository;
import bpellint.core.model.Navigator;
import bpellint.core.model.NodeHelper;
import bpellint.core.model.NodeToId;
import bpellint.core.model.ProcessContainer;
import bpellint.core.model.Referable;
import nu.xom.Node;

import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class Validator {

	private static final int DEFAULT_TYPE = 1;

	protected final ProcessContainer processContainer;
	protected final Navigator navigator;
	private final SimpleValidationResult validationCollector;

	private static final ErrorMessageRepository errorMessageRepository = new ErrorMessageRepository();

	public Validator(ProcessContainer processContainer,
	                 SimpleValidationResult validationCollector) {
		this.processContainer = processContainer;
		this.validationCollector = validationCollector;
		navigator = new Navigator(processContainer);
	}

	public abstract void validate();

	public abstract int getSaNumber();

	protected void addWarning(Referable node, String message) {
		addWarning(node.toXOM(), message);
    }

	protected void addWarning(Node node, String message) {
		Path fileName = normalizeFilePath(node);
		NodeToId identifiableNode = new NodeToId(node);

		Location location = new Location(fileName, new LocationCoordinate(identifiableNode.getLineNumber(), identifiableNode.getColumnNumber()), new NodeHelper(node).getXpath());
		validationCollector.addWarning(new Warning(message, location));
    }

	private void addViolation(Path fileName, Node node, int type) {
		NodeToId identifiableNode = new NodeToId(node);

		Location location = new Location(fileName, new LocationCoordinate(identifiableNode.getLineNumber(), identifiableNode.getColumnNumber()), new NodeHelper(node).getXpath());
		// TODO add type message instead of just the type
		String saNumber = numberToSAConstraint(getSaNumber());
		String message;
		try {
			message = errorMessageRepository.getShort(saNumber, type);
		} catch (DescriptionNotFoundException e) {
			message = "";
		}
		validationCollector.addViolation(new Violation(location, message, saNumber));
	}

	public static String numberToSAConstraint(int number) {
		return String.format("SA%05d", number);
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
		addViolation(normalizeFilePath(node), node, type);
	}

	private Path normalizeFilePath(Node node) {
		String nodeURI = new NodeHelper(node).getFilePath();
		String prefix = "file:///";
		if(nodeURI.startsWith(prefix)) {
			return Paths.get(nodeURI.substring(prefix.length()));
		} else {
			return Paths.get(nodeURI);
		}
	}

	public String getBpelFileName() {
		return processContainer.getBpel().getFilePath().getFileName().toString();
	}

}
