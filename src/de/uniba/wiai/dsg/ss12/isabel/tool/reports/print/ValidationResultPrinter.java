package de.uniba.wiai.dsg.ss12.isabel.tool.reports.print;

import de.uniba.wiai.dsg.ss12.isabel.tool.ValidationException;
import de.uniba.wiai.dsg.ss12.isabel.tool.VerbosityLevel;
import de.uniba.wiai.dsg.ss12.isabel.tool.reports.Violation;
import de.uniba.wiai.dsg.ss12.isabel.tool.reports.ViolationCollector;
import nu.xom.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class ValidationResultPrinter {

	private Document errorDocument = null;
	private final XPathContext MSG_CONTEXT = new XPathContext("err",
			"http://lspi.wiai.uni-bamberg.de/ed-12-ss-proj-bpelval/schema/errormessages");

	public ValidationResultPrinter() {
		try {
			errorDocument = loadErrorMessages();
		} catch (ValidationException e) {
			System.out.println("Warning: Could not load static-analysis-violation-descriptions.");
		}
	}

	public void printResults(VerbosityLevel verbosityLevel, ViolationCollector violationCollector) {
		switch (verbosityLevel) {
			case NORMAL:
				printResults(violationCollector);
				break;
			case VERBOSE:
				printResultsVerbose(violationCollector);
				break;
			case FULL:
				printResultsFull(violationCollector);
				break;
		}
	}

	private Document loadErrorMessages() throws ValidationException {
		Builder parser = new Builder();
		try {

			InputStream errorMessageStream = ValidationResultPrinter.class.getResourceAsStream("/errormessages.xml");
			if (errorMessageStream == null) {
				throw new ValidationException("Unable to load errormessages.xml, "
						+ "the file was not found");
			}

			try (InputStreamReader reader = new InputStreamReader(errorMessageStream)) {
				return parser.build(reader);
			}
		} catch (ValidityException e) {
			throw new ValidationException(e, "Unable to load errormessages.xml, "
					+ "this file is invalid");
		} catch (ParsingException e) {
			throw new ValidationException(e, "Unable to load errormessages.xml, "
					+ "this file could not be parsed");
		} catch (FileNotFoundException e) {
			throw new ValidationException(e, "Unable to load errormessages.xml, "
					+ "the file was not found");
		} catch (IOException e) {
			throw new ValidationException(e, "Unable to load errormessages.xml, "
					+ "the reading unit is damaged");
		}
	}

	private void printResults(ViolationCollector violationCollector) {
		List<Violation> violations = violationCollector.getResults();

		printOutputHeader(violations);
		String previousSourceFile = "";
		for (Violation violation : violations) {
			String saNumber = getSANumber(violation);
			printLineRowSA(violation, saNumber, previousSourceFile);
			printShortDescription(saNumber, violation.type);
			previousSourceFile = violation.fileName;
		}
	}

	private void printOutputHeader(List<Violation> violations) {
		if (!violations.isEmpty()) {
			System.out.println("VIOLATIONS:\n");
			if (errorDocument == null) {
				printErrorDocumentFatal();
			}
		}
	}

	private String getSANumber(Violation violation) {
		String saNumber = "SA000";
		saNumber = (violation.ruleNumber < 10) ? saNumber + "0" : saNumber;
		return saNumber + violation.ruleNumber;
	}

	private void printResultsVerbose(ViolationCollector violationCollector) {
		List<Violation> violations = violationCollector.getResults();

		printOutputHeader(violations);
		String previousSourceFile = "";
		for (Violation violation : violations) {
			String saNumber = getSANumber(violation);
			printLineRowSA(violation, saNumber, previousSourceFile);
			printLongDescription(saNumber);
			previousSourceFile = violation.fileName;
		}
	}

	private void printResultsFull(ViolationCollector violationCollector) {
		List<Violation> violations = violationCollector.getResults();

		printOutputHeader(violations);
		String previousSourceFile = "";
		for (Violation violation : violations) {
			String saNumber = getSANumber(violation);
			printLineRowSA(violation, saNumber, previousSourceFile);
			printShortDescription(saNumber, violation.type);
			printLongDescription(saNumber);
			previousSourceFile = violation.fileName;
		}
	}

	private void printLineRowSA(Violation violation, String saNumber, String previousSourceFile) {
		if (!violation.fileName.equals(previousSourceFile))
			System.out.println(violation.fileName + ":");

		System.out.println("\tLine " + violation.row + ", Column " + violation.column + ", "
				+ saNumber);
	}

	private void printShortDescription(String SANumber, int type) {
		if (errorDocument == null) {
			return;
		}
		String shortDescription;
		Nodes nodes = errorDocument.query("//err:rule[@name='" + SANumber
				+ "']/err:shorts/err:type[err:number=" + type + "]/err:content", MSG_CONTEXT);
		try {
			shortDescription = nodes.get(0).getValue().trim();
		} catch (IndexOutOfBoundsException e) {
			shortDescription = "error message not found (" + SANumber + ")";
		}
		System.out.println("\t\t" + shortDescription);
	}

	private void printLongDescription(String SANumber) {
		if (errorDocument == null) {
			return;
		}
		String longDescription;
		Nodes nodes = errorDocument.query("//err:rule[@name='" + SANumber + "']/err:long",
				MSG_CONTEXT);
		try {
			longDescription = nodes.get(0).getValue().trim();
		} catch (IndexOutOfBoundsException e) {
			longDescription = "long description not found (" + SANumber + ")";
		}
		System.out.println("\t\t" + longDescription);
	}

	private void printErrorDocumentFatal() {
		System.out.println("Warning: Error message document is not specified.");
	}

	public void printStartUpError(Exception e) {
		System.err.println("Error: " + e.getMessage());
	}
}
