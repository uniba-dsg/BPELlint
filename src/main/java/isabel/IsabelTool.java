package isabel;

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

import isabel.io.CommandLineInterpreter;
import isabel.io.ValidationResultPrinter;
import isabel.io.VerbosityLevel;
import isabel.tool.Isabel;
import isabel.tool.ValidationException;
import isabel.tool.ValidationResult;
import org.pmw.tinylog.Logger;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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