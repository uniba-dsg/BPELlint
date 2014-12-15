package bpellint.ui;

import org.pmw.tinylog.Logger;

import bpellint.core.BpelLint;
import api.ValidationException;
import api.ValidationResult;
import api.Validator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class BpelLintTool {

    public static void main(String[] args) {
        try {
            CommandLineInterpreter input = new CommandLineInterpreter();
            CLIOptions options = input.parse(args);

            Validator bpelLint = buildValidator(options);

            for(String path : options.paths) {
                validate(Paths.get(path), bpelLint);
            }

        } catch (Exception e) {
            Logger.info(e);
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static Validator buildValidator(CLIOptions options) throws ValidationException {
        if(options.schemaValidation) {
            return new BpelLint();
        } else {
            return BpelLint.buildWithoutSchemaValidation();
        }
    }

    private static void validate(Path path, Validator bpelLint)
            throws IOException {
        Files.walk(path).filter(BpelLintTool::isBpelFile).forEach(p -> {
            try {
                ValidationResult validationResult = bpelLint.validate(p);
                new SeparateLineValidationResultPrinter().print(validationResult);
            } catch (Exception e) {
                Logger.info(e);
                System.out.println("Error: " + e.getMessage());
            }
        });
    }

    private static boolean isBpelFile(Path path) {
        return Files.isRegularFile(path) && path.toString().endsWith(".bpel");
    }
}