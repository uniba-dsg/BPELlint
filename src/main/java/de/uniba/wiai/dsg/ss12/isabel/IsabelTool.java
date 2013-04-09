package de.uniba.wiai.dsg.ss12.isabel;

/*
 *#####################################################################
 *                      #                                             #
 *            ;@,       #              I S A B E L                    #
 *     ,        *@,     #                                             #
 *   ;#     ,@@,  *@    #(I statically  analyze BPEL-files flawlessly)#
 *  #*      *@#*  ,@    #                                             #
 *  #            @&     #                                             #
 *  #,     ,@@,   '@    #                                             #
 *   *#    *@#*   ,@    #                                             #
 *    ^        ,,@*     #                                             #
 *            ***       #                                             #
 *                      #                                             #
 *#####################################################################
 */

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.pmw.tinylog.Logger;

import de.uniba.wiai.dsg.ss12.isabel.io.CommandLineInterpreter;
import de.uniba.wiai.dsg.ss12.isabel.io.ValidationResultPrinter;
import de.uniba.wiai.dsg.ss12.isabel.io.VerbosityLevel;
import de.uniba.wiai.dsg.ss12.isabel.tool.Isabel;
import de.uniba.wiai.dsg.ss12.isabel.tool.ValidationException;
import de.uniba.wiai.dsg.ss12.isabel.tool.ValidationResult;

public class IsabelTool {

	private static ValidationResultPrinter validationResultPrinter = new ValidationResultPrinter();

	public static void main(String[] args) {
		try {
			CommandLineInterpreter input = new CommandLineInterpreter(args);
			validate(Paths.get(input.path), input.verbosityLevel);
		} catch (Exception e) {
			Logger.info(e);
			System.out.println("Error: " + e.getMessage());
		}
	}

	private static void validate(Path path, VerbosityLevel verbosityLevel)
			throws ValidationException, IOException {
		if (Files.isRegularFile(path) && path.toString().endsWith(".bpel")) {
			try {
				ValidationResult validationResult = new Isabel().validate(path
						.toString());
				validationResultPrinter.printResults(verbosityLevel,
						validationResult);
			} catch (Exception e) {
				Logger.info(e);
				System.out.println("Error: " + e.getMessage());
			}
		} else if (Files.isDirectory(path)) {
			// file tree iteration
			try (DirectoryStream<Path> directoryPaths = Files
					.newDirectoryStream(path)) {
				for (Path directoryPath : directoryPaths) {
					validate(directoryPath, verbosityLevel);
				}
			}
		}
	}
}