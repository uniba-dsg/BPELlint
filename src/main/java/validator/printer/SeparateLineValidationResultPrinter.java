package validator.printer;


import com.google.common.base.Charsets;
import validator.ValidationResult;
import validator.Violation;
import validator.Warning;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/**
 * Prints each violation and warning on a separate line similar to unix tools to simplify further processing.
 */
public class SeparateLineValidationResultPrinter {

    private PrintStream printer;

    public SeparateLineValidationResultPrinter() {
        this(System.out);
    }

    public static void printToFile(Path file, ValidationResult validationResult) {
        try(OutputStream os = Files.newOutputStream(file)) {
            new SeparateLineValidationResultPrinter(new PrintStream(os)).print(validationResult);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public SeparateLineValidationResultPrinter(PrintStream printer) {
        Objects.requireNonNull(printer);

        this.printer = printer;
    }

    public void print(ValidationResult validationResult) {
        Objects.requireNonNull(validationResult);

        validationResult.getViolations().forEach(this::print);
        validationResult.getWarnings().forEach(this::print);
    }

    private void print(Warning warning) {
        printer.format("WARNING %s [%s]: %s%n", warning.getIndicator().getFileName(), warning.getIndicator().getLocation().getId(), warning.getMessage());
    }

    private void print(Violation violation) {
        printer.format("VIOLATION %s [%s]: %s %s%n", violation.getIndicator().getFileName(), violation.getIndicator().getLocation().getId(), violation.getConstraint(), violation.getMessage());
    }

}
