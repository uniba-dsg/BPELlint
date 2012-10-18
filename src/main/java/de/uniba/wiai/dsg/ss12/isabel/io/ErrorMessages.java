package de.uniba.wiai.dsg.ss12.isabel.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ErrorMessages {

	public String getShort(String saNumber, int type)
			throws DescriptionNotFoundException {
		try (Scanner scanner = new Scanner(getFilePath(saNumber))) {
			return getShortByType(type, scanner);

		} catch (FileNotFoundException e) {
			throw new DescriptionNotFoundException(
					"Expected File does not exist.");
		}
	}

	private File getFilePath(String saNumber) {
		return new File("Testcases/" + saNumber + "/" + saNumber + ".txt");
	}

	private String getShortByType(int type, Scanner scanner)
			throws DescriptionNotFoundException {
		for (int i = 1; scanner.hasNextLine(); i++) {
			String nextLine = scanner.nextLine();
			if (i == type)
				return nextLine;
		}

		throw new DescriptionNotFoundException("Expected Short does not exist.");
	}

	public String getLong(String saNumber) throws DescriptionNotFoundException {
		try (Scanner scanner = new Scanner(getFilePath(saNumber))) {
			return skipLinesUntilLong(scanner);

		} catch (FileNotFoundException e) {
			throw new DescriptionNotFoundException(
					"Expected File does not exist.");
		}
	}

	private String skipLinesUntilLong(Scanner scanner)
			throws DescriptionNotFoundException {
		while (scanner.hasNextLine()) {
			String nextLine = scanner.nextLine();
			if ("@@@".equals(nextLine) && scanner.hasNextLine())
				return scanner.nextLine();
		}

		throw new DescriptionNotFoundException("Expected Long does not exist.");
	}
}
