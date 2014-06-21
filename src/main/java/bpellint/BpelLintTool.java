package bpellint;

import org.pmw.tinylog.Logger;

import bpellint.io.CLIOptions;
import bpellint.io.CommandLineInterpreter;
import bpellint.io.ValidationResultPrinter;
import bpellint.io.VerbosityLevel;
import bpellint.tool.BpelLint;
import bpellint.tool.validators.ValidationException;
import bpellint.tool.validators.result.ValidationResult;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class BpelLintTool {

    private static final ValidationResultPrinter validationResultPrinter = new ValidationResultPrinter();
    private static boolean pathContainsBPELfiles = false;

    public static void main(String[] args) {
        try {
            CommandLineInterpreter input = new CommandLineInterpreter();
            CLIOptions options = input.parse(args);

            BpelLint bpelLint = buildValidator(options);

            for(String path : options.paths) {
                validate(Paths.get(path), options.verbosityLevel, bpelLint);
            }

            if (!pathContainsBPELfiles) {
            	throw new IOException("could not find file under " + Arrays.toString(options.paths));
			}

        } catch (Exception e) {
            Logger.info(e);
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static BpelLint buildValidator(CLIOptions options) throws ValidationException {
        if(options.schemaValidation) {
            return new BpelLint();
        } else {
            return BpelLint.buildWithoutSchemaValidation();
        }
    }

    private static void validate(Path path, VerbosityLevel verbosityLevel, BpelLint bpelLint)
            throws IOException {
        if (isBpelFile(path)) {
            try {
            	pathContainsBPELfiles = true;
                ValidationResult validationResult = bpelLint.validate(path);
                validationResultPrinter.printResults(verbosityLevel, validationResult);
            } catch (Exception e) {
                Logger.info(e);
                System.out.println("Error: " + e.getMessage());
            }
        } else if (Files.isDirectory(path)) {
            // file tree iteration
            try (DirectoryStream<Path> directoryPaths = Files.newDirectoryStream(path)) {
                for (Path directoryPath : directoryPaths) {
                    validate(directoryPath, verbosityLevel, bpelLint);
                }
            }
        }
    }

    private static boolean isBpelFile(Path path) {
        return Files.isRegularFile(path) && path.toString().endsWith(".bpel");
    }
}