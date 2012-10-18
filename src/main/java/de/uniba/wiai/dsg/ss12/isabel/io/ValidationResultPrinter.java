package de.uniba.wiai.dsg.ss12.isabel.io;

import java.util.List;

import de.uniba.wiai.dsg.ss12.isabel.tool.ValidationResult;
import de.uniba.wiai.dsg.ss12.isabel.tool.Violation;

public class ValidationResultPrinter {

	private final ErrorMessageRepository errorMessageRepository = new ErrorMessageRepository();

	public void printResults(VerbosityLevel verbosityLevel,
			ValidationResult violationCollector) {
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

	private void printResults(ValidationResult violationCollector) {
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
		}
	}

	private String getSANumber(Violation violation) {
		String saNumber = "SA000";
		saNumber = (violation.ruleNumber < 10) ? saNumber + "0" : saNumber;
		return saNumber + violation.ruleNumber;
	}

	private void printResultsVerbose(ValidationResult violationCollector) {
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

	private void printResultsFull(ValidationResult violationCollector) {
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

	private void printLineRowSA(Violation violation, String saNumber,
			String previousSourceFile) {
		if (!violation.fileName.equals(previousSourceFile))
			System.out.println(violation.fileName + ":");

		System.out.println("\tLine " + violation.row + ", Column "
				+ violation.column + ", " + saNumber);
	}

	private void printShortDescription(String SANumber, int type) {
		try {
			System.out.println("\t\t" + errorMessageRepository.getShort(SANumber, type));
		} catch (DescriptionNotFoundException e) {
			printErrorDocumentFatal(e.getMessage());
		}
	}

	private void printLongDescription(String SANumber) {
		try {
			System.out.println("\t\t" + errorMessageRepository.getLong(SANumber));
		} catch (DescriptionNotFoundException e) {
			printErrorDocumentFatal(e.getMessage());
		}
	}

	private void printErrorDocumentFatal(String error) {
		System.out.println("Warning: " + error);
	}

}
