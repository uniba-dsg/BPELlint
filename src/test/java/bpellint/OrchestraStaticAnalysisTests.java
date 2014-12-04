package bpellint;


import bpellint.tool.validators.result.SimpleValidationResult;
import org.junit.Test;
import org.junit.runners.Parameterized;

import validator.printer.SeparateLineValidationResultPrinter;
import bpellint.tool.BpelLint;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class OrchestraStaticAnalysisTests extends FunctionalValidatorTests{

    public OrchestraStaticAnalysisTests(String bpel, String violatedRules) {
        super(bpel, violatedRules);
    }

    @Parameterized.Parameters(name = "{index}: {0} violates {1}")
    public static Collection<Object[]> data() throws IOException {
        List<Object[]> bpelFiles = new LinkedList<>();
        bpelFiles.addAll(SAViolationOrchestraTestData.orchestraSATests());

        return bpelFiles;
    }

    @Override
	@Test
    public void testValidators() throws Exception {
        SimpleValidationResult validationResult = (SimpleValidationResult) BpelLint.buildWithoutSchemaValidation().validate(Paths.get(bpel));

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        new SeparateLineValidationResultPrinter(ps).print(validationResult);
        String data = "\n" + baos.toString() + "\n";

        assertEquals("BPEL: " + bpel + data, violatedRules, validationResult.getViolatedRules());
    }

}
