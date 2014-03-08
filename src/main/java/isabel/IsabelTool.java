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

import isabel.io.CLIOptions;
import isabel.io.CommandLineInterpreter;
import isabel.io.ValidationResultPrinter;
import isabel.io.VerbosityLevel;
import isabel.tool.Isabel;
import isabel.tool.validators.ValidationException;
import isabel.tool.validators.result.ValidationResult;

import org.pmw.tinylog.Logger;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class IsabelTool {

    private static final ValidationResultPrinter validationResultPrinter = new ValidationResultPrinter();
    private static boolean pathContainsBPELfiles = false;

    public static void main(String[] args) {
        try {
            CommandLineInterpreter input = new CommandLineInterpreter();
            CLIOptions options = input.parse(args);

            Isabel isabel = buildValidator(options);

            for(String path : options.paths) {
                validate(Paths.get(path), options.verbosityLevel, isabel);
            }

            if (!pathContainsBPELfiles) {
            	throw new IOException("could not find file under " + Arrays.toString(options.paths));
			}

        } catch (Exception e) {
            Logger.info(e);
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static Isabel buildValidator(CLIOptions options) throws ValidationException {
        if(options.schemaValidation) {
            return new Isabel();
        } else {
            return Isabel.buildWithoutSchemaValidation();
        }
    }

    private static void validate(Path path, VerbosityLevel verbosityLevel, Isabel isabel)
            throws IOException {
        if (isBpelFile(path)) {
            try {
            	pathContainsBPELfiles = true;
                ValidationResult validationResult = isabel.validate(path);
                validationResultPrinter.printResults(verbosityLevel, validationResult);
            } catch (Exception e) {
                Logger.info(e);
                System.out.println("Error: " + e.getMessage());
            }
        } else if (Files.isDirectory(path)) {
            // file tree iteration
            try (DirectoryStream<Path> directoryPaths = Files.newDirectoryStream(path)) {
                for (Path directoryPath : directoryPaths) {
                    validate(directoryPath, verbosityLevel, isabel);
                }
            }
        }
    }

    private static boolean isBpelFile(Path path) {
        return Files.isRegularFile(path) && path.toString().endsWith(".bpel");
    }
}