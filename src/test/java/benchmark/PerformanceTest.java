package benchmark;

import bpellint.io.EnvironmentVariableInterpreter;
import bpellint.tool.BpelLint;
import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;
import org.junit.*;
import org.junit.rules.TestRule;
import org.pmw.tinylog.Logger;
import org.pmw.tinylog.LoggingLevel;

import java.nio.file.Paths;


/**
 * Executes tests based on the files from within the /Testcases directory.
 */

public class PerformanceTest {

    @Rule
    public TestRule benchmark = new BenchmarkRule();

    @Before
    public void setUp() throws Exception {
        Logger.setLoggingLevel(LoggingLevel.OFF);
        System.setProperty(EnvironmentVariableInterpreter.BPEL_LINT_SA_RULES_ENVIRONMENT_VARIABLE, "ALL");
    }

    @After
    public void tearDown() throws Exception {
        System.clearProperty(EnvironmentVariableInterpreter.BPEL_LINT_SA_RULES_ENVIRONMENT_VARIABLE);
    }

    @BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 10)
    @Test
	public void measureSimpleProcess() throws Exception {
		new BpelLint().validate(Paths.get("Testcases/betsy/structured/Sequence.bpel"));
	}

    @BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 10)
    @Test
    public void measureMediumProcess() throws Exception {
        new BpelLint().validate(Paths.get("Testcases/betsy/structured/Flow-GraphExample.bpel"));
    }

    @BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 10)
    @Test
    public void measureMediumHardProcess() throws Exception {
        new BpelLint().validate(Paths.get("Testcases/complex/requestor/prototype-ebBP-BT-Requestor.bpel"));
    }

    @BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 10)
    @Test
    public void measureComplexProcess() throws Exception {
        new BpelLint().validate(Paths.get("Testcases/complex/bpel_resp/Transaction-rev1-btaRAandAAandTTPv1-role-2.bpel"));
    }

}
