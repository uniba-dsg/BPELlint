package de.uniba.wiai.dsg.ss12.isabel;

import java.io.File;
import java.util.List;

import de.uniba.wiai.dsg.ss12.isabel.tool.Isabel;
import de.uniba.wiai.dsg.ss12.isabel.tool.ValidationException;
import de.uniba.wiai.dsg.ss12.isabel.tool.VerbosityLevel;
import de.uniba.wiai.dsg.ss12.isabel.tool.reports.print.ValidationResultPrinter;
import de.uniba.wiai.dsg.ss12.isabel.tool.schemaValidation.io.DirectoryScanner;

/**
 * Test Isabel versus all Betsy Tests.
 */
public class TestIsabelWithBetsyTests {

	public static final String BETSY_TESTS_PATH = "C:\\Users\\stcpreissinger\\Desktop\\openesb";

	public static void main(String[] args) throws ValidationException {
		List<File> bpelFiles = new DirectoryScanner(new File(BETSY_TESTS_PATH),
				".bpel").scan();
		for (File bpelFile : bpelFiles) {
			validateBpelFile(bpelFile);
		}

	}

	private static void validateBpelFile(File bpelFile)
			throws ValidationException {
		ValidationResultPrinter violationPrinter = new ValidationResultPrinter();
		violationPrinter.printResults(VerbosityLevel.NORMAL,
				new Isabel().validate(bpelFile.getAbsolutePath()));
	}
}
