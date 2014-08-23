package benchmark;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class PerformanceTestData {

    public static List<Object[]> saViolationTests() {
        Object[][] data = new Object[][]{

                //{"Testcases/rules/SA00001/Notification.bpel", "1"},
                //{"Testcases/rules/SA00060/OnEventFiloSecondMexMissing.bpel", "60"},
                //{"Testcases/calculator/CalculatorService.bpel", ""},
                {"Testcases/complex/bpel_req/Transaction-rev1-btaRAandAAandTTPv1-role-1.bpel", ""},

        };
        return Arrays.asList(data);
    }
}
