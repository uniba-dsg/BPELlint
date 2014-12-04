package bpellint;


import validator.printer.SeparateLineValidationResultPrinter;
import bpellint.tool.BpelLint;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import validator.ValidationException;
import validator.ValidationResult;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Executes tests based on the files from within the /Testcases directory.
 */
@RunWith(value = Parameterized.class)
public class XsdSchemaValidatorTests {

    private final String bpel;

    public XsdSchemaValidatorTests(String bpel) {
        this.bpel = bpel;
    }

    @Parameterized.Parameters(name = "{index}: {0}")
    public static Collection<Object[]> data() {
        Object[][] data = new Object[][]{
                {"Testcases/rules/SA00024/OnEvent-containing-dot_IGNORE_ME.bpel"},
                {"Testcases/rules/SA00024/Variable-containing-dot.bpel"},
                {"Testcases/rules/SA00024/CatchContainingDot_IGNORE_ME.bpel"},
        };
        return Arrays.asList(data);
    }

    @Test
    public void testValidators() throws Exception {
        try {
            ValidationResult validationResult = new BpelLint().validate(Paths.get(bpel));

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(baos);
            new SeparateLineValidationResultPrinter(ps).print(validationResult);
            fail("BPEL: " + bpel + " must throw an XSD error!");
        } catch (ValidationException e) {
            assertTrue(true);
        }
    }

}
