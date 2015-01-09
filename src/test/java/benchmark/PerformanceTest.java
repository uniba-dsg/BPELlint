package benchmark;

import bpellint.core.validators.rules.infos.EnvironmentVariableInterpreter;
import bpellint.core.BpelLint;
import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;
import org.junit.*;
import org.junit.rules.TestRule;
import org.junit.runners.MethodSorters;
import org.pmw.tinylog.Logger;
import org.pmw.tinylog.LoggingLevel;

import java.nio.file.Paths;


/**
 * Executes tests based on the files from within the /Testcases directory.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
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
	public void measureSequence() throws Exception {
		new BpelLint().validate(Paths.get("Testcases/betsy/structured/Sequence.bpel"));
	}

    @BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 10)
    @Test
    public void measureFlowGraphExample() throws Exception {
        new BpelLint().validate(Paths.get("Testcases/betsy/structured/Flow-GraphExample.bpel"));
    }

    @BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 10)
    @Test
    public void measureInvokeSync() throws Exception {
        new BpelLint().validate(Paths.get("Testcases/betsy/basic/Invoke-Sync.bpel"));
    }

    @BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 10)
    @Test
    public void measureValidate() throws Exception {
        new BpelLint().validate(Paths.get("Testcases/betsy/basic/Validate.bpel"));
    }

    @BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 10)
    @Test
    public void measurePrototypeRequestor() throws Exception {
        new BpelLint().validate(Paths.get("Testcases/complex/requestor/prototype-ebBP-BT-Requestor.bpel"));
    }

    @BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 10)
    @Test
    public void measurePrototypeResponder() throws Exception {
        new BpelLint().validate(Paths.get("Testcases/complex/responder/prototype-ebBP-BT-Responder.bpel"));
    }

    @BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 10)
    @Test
    public void measureTransactionResponder() throws Exception {
        new BpelLint().validate(Paths.get("Testcases/complex/bpel_resp/Transaction-rev1-btaRAandAAandTTPv1-role-2.bpel"));
    }

    @BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 10)
    @Test
    public void measureTransactionRequestor() throws Exception {
        new BpelLint().validate(Paths.get("Testcases/complex/bpel_req/Transaction-rev1-btaRAandAAandTTPv1-role-1.bpel"));
    }

    @BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 10)
    @Test
    public void measureTravelReservationService() throws Exception {
        new BpelLint().validate(Paths.get("Testcases/openesb/travel-reservation-service/TravelReservationService.bpel"));
    }

    @BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 10)
    @Test
    public void measureBlueprint5() throws Exception {
        new BpelLint().validate(Paths.get("Testcases/openesb/blueprint5/ReservationSystem.bpel"));
    }

    @BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 10)
    @Test
    public void measureBlueprint4() throws Exception {
        new BpelLint().validate(Paths.get("Testcases/openesb/blueprint4/POService.bpel"));
    }

}
