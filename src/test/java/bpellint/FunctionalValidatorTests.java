package bpellint;


import bpellint.core.validators.rules.infos.EnvironmentVariableInterpreter;
import bpellint.ui.SeparateLineValidationResultPrinter;
import bpellint.core.BpelLint;
import api.SimpleValidationResult;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Paths;
import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * Executes tests based on the files from within the /Testcases directory.
 */
@RunWith(value = Parameterized.class)
public class FunctionalValidatorTests {

    protected final Set<Integer> violatedRules;
    protected final String bpel;

    public FunctionalValidatorTests(String bpel, String violatedRules) {
        this.violatedRules = parseString(violatedRules);
        this.bpel = bpel;
    }

    private Set<Integer> parseString(String violatedRules) {
        String[] elements = violatedRules.split(",");
        Set<Integer> parsedElements = new HashSet<>();
        for (String element : elements) {
            if (element == null || element.isEmpty()) {
                continue;
            }
            parsedElements.add(Integer.parseInt(element.trim()));
        }
        return parsedElements;
    }

    @Parameterized.Parameters(name = "{index}: {0} violates {1}")
    public static Collection<Object[]> data() throws IOException {
        List<Object[]> bpelFiles = new LinkedList<>();
        bpelFiles.addAll(SAViolationTestData.saViolationTests());
        bpelFiles.addAll(new HappyPathTestCreator(Paths.get("Testcases/betsy")).list());
        bpelFiles.addAll(new HappyPathTestCreator(Paths.get("Testcases/calculator")).list());
        bpelFiles.addAll(new HappyPathTestCreator(Paths.get("Testcases/complex")).list());

        return bpelFiles;
    }

    @Before
    public void setUp() throws Exception {
        System.setProperty(EnvironmentVariableInterpreter.BPEL_LINT_SA_RULES_ENVIRONMENT_VARIABLE, "ALL");
    }

    @After
    public void tearDown() throws Exception {
        System.clearProperty(EnvironmentVariableInterpreter.BPEL_LINT_SA_RULES_ENVIRONMENT_VARIABLE);
    }

    @Test
    public void testValidators() throws Exception {
        SimpleValidationResult validationResult = (SimpleValidationResult) new BpelLint().validate(Paths.get(bpel));

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        new SeparateLineValidationResultPrinter(ps).print(validationResult);
        String data = "\n" + baos.toString() + "\n";

        assertEquals("BPEL: " + bpel + data, violatedRules, validationResult.getViolatedRules());
    }

}
