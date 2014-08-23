package benchmark;


import bpellint.SAViolationTestData;
import bpellint.io.EnvironmentVariableInterpreter;
import bpellint.io.ValidationResultPrinter;
import bpellint.io.VerbosityLevel;
import bpellint.tool.BpelLint;
import bpellint.tool.validators.result.ValidationResult;
import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;
import org.junit.*;
import org.junit.rules.TestRule;
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
public class PerformanceTest {
    @Rule
    public TestRule benchmark = new BenchmarkRule();

	protected final Set<Integer> violatedRules;
    protected final String bpel;

	public PerformanceTest(String bpel, String violatedRules) {
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
		return PerformanceTestData.saViolationTests();
	}

    @Before
    public void setUp() throws Exception {
        System.setProperty(EnvironmentVariableInterpreter.BPEL_LINT_SA_RULES_ENVIRONMENT_VARIABLE, "ALL");
    }

    @After
    public void tearDown() throws Exception {
        System.clearProperty(EnvironmentVariableInterpreter.BPEL_LINT_SA_RULES_ENVIRONMENT_VARIABLE);
    }

    @BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 10)
    @Test
	public void testValidators() throws Exception {
		ValidationResult validationResult = new BpelLint().validate(Paths.get(bpel));

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		new ValidationResultPrinter(ps).printResults(VerbosityLevel.NORMAL,
				validationResult);
		String data = "\n" + baos.toString() + "\n";

		assertEquals("BPEL: " + bpel + data, violatedRules,
				validationResult.getViolatedRules());
	}

}
