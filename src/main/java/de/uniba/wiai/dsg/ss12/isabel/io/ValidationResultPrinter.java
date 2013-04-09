package de.uniba.wiai.dsg.ss12.isabel.io;

import java.io.PrintStream;
import java.util.List;

import de.uniba.wiai.dsg.ss12.isabel.tool.ValidationResult;
import de.uniba.wiai.dsg.ss12.isabel.tool.Violation;

public class ValidationResultPrinter {

	private final ErrorMessageRepository errorMessageRepository = new ErrorMessageRepository();
	private PrintStream printer;

	public ValidationResultPrinter() {
		this(System.out);
	}

	public ValidationResultPrinter(PrintStream printer) {
		this.printer = printer;
	}

	public void printResults(VerbosityLevel verbosityLevel,
			ValidationResult validationResult) {
		switch (verbosityLevel) {
		case NORMAL:
			printResults(validationResult);
			break;
		case FULL:
			printResultsFull(validationResult);
			break;
		}
	}

	private void printResults(ValidationResult validationResult) {
		List<Violation> violations = validationResult.getViolations();

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
			printer.println("VIOLATIONS:\n");
		}
	}

	private String getSANumber(Violation violation) {
		String saNumber = "SA000";
		saNumber = (violation.ruleNumber < 10) ? saNumber + "0" : saNumber;
		return saNumber + violation.ruleNumber;
	}

	private void printResultsFull(ValidationResult validationResult) {
		List<Violation> violations = validationResult.getViolations();

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
			printer.println(violation.fileName + ":");

		printer.println("\tLine " + violation.row + ", Column "
				+ violation.column + ", " + saNumber);
	}

	private void printShortDescription(String SANumber, int type) {
		try {
			printer.println("\t\t"
					+ errorMessageRepository.getShort(SANumber, type));
		} catch (DescriptionNotFoundException e) {
			printErrorDocumentFatal(e.getMessage());
		}
	}

	private void printLongDescription(String SANumber) {
		try {
			printer.println("\t\t" + errorMessageRepository.getLong(SANumber));
		} catch (DescriptionNotFoundException e) {
			printErrorDocumentFatal(e.getMessage());
		}
	}

	private void printErrorDocumentFatal(String error) {
		printer.println("Warning: " + error);
	}

}
