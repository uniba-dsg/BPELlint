package bpellint;


import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.Parameterized;

import bpellint.io.ValidationResultPrinter;
import bpellint.io.VerbosityLevel;
import bpellint.tool.BpelLint;
import bpellint.tool.validators.result.ValidationResult;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;


public class BPEL2oWFNTests extends FunctionalValidatorTests{

    public BPEL2oWFNTests(String bpel, String violatedRules) {
        super(bpel, violatedRules);
    }

    @Parameterized.Parameters(name = "{index}: {0} violates {1}")
    public static Collection<Object[]> data() throws IOException {
        List<Object[]> bpelFiles = new LinkedList<>();
        bpelFiles.addAll(BPEL2oWFNTests.satests());
        bpelFiles.addAll(new HappyPathTestCreator(Paths.get("Testcases/bpel2owfn/testfiles")).list());

        return bpelFiles;
    }

    public static List<Object[]> satests() {
        Object[][] data = new Object[][]{

                {"Testcases/bpel2owfn/sa_tests/sa00002.bpel", "2"},
                {"Testcases/bpel2owfn/sa_tests/sa00003.bpel", "3"},
                {"Testcases/bpel2owfn/sa_tests/sa00005.bpel", "5"},
                {"Testcases/bpel2owfn/sa_tests/sa00006.bpel", "6"},
                {"Testcases/bpel2owfn/sa_tests/sa00007.bpel", "7"},
                {"Testcases/bpel2owfn/sa_tests/sa00008.bpel", "8"},
                {"Testcases/bpel2owfn/sa_tests/sa00015.bpel", "15"},
                {"Testcases/bpel2owfn/sa_tests/sa00016.bpel", "16"},
                {"Testcases/bpel2owfn/sa_tests/sa00017.bpel", "17"},
                {"Testcases/bpel2owfn/sa_tests/sa00018.bpel", "18"},
                {"Testcases/bpel2owfn/sa_tests/sa00023.bpel", "23"},
                {"Testcases/bpel2owfn/sa_tests/sa00024.bpel", "24"},
                {"Testcases/bpel2owfn/sa_tests/sa00025.bpel", "25"},
                {"Testcases/bpel2owfn/sa_tests/sa00032.bpel", "32"},
                {"Testcases/bpel2owfn/sa_tests/sa00035.bpel", "35"},
                {"Testcases/bpel2owfn/sa_tests/sa00036.bpel", "36"},
                {"Testcases/bpel2owfn/sa_tests/sa00037.bpel", "37"},
                {"Testcases/bpel2owfn/sa_tests/sa00044.bpel", "44"},
                {"Testcases/bpel2owfn/sa_tests/sa00048.bpel", "48"},
                {"Testcases/bpel2owfn/sa_tests/sa00051.bpel", "51"},
                {"Testcases/bpel2owfn/sa_tests/sa00052.bpel", "52"},
                {"Testcases/bpel2owfn/sa_tests/sa00055.bpel", "55"},
                {"Testcases/bpel2owfn/sa_tests/sa00056.bpel", "56"},
                {"Testcases/bpel2owfn/sa_tests/sa00057.bpel", "57"},
                {"Testcases/bpel2owfn/sa_tests/sa00058.bpel", "58"},
                {"Testcases/bpel2owfn/sa_tests/sa00059.bpel", "59"},
                {"Testcases/bpel2owfn/sa_tests/sa00060.bpel", "60"},
                {"Testcases/bpel2owfn/sa_tests/sa00061.bpel", "61"},
                {"Testcases/bpel2owfn/sa_tests/sa00062.bpel", "62"},
                {"Testcases/bpel2owfn/sa_tests/sa00063.bpel", "63"},
                {"Testcases/bpel2owfn/sa_tests/sa00064.bpel", "64"},
                {"Testcases/bpel2owfn/sa_tests/sa00065.bpel", "65"},
                {"Testcases/bpel2owfn/sa_tests/sa00066.bpel", "66"},
                {"Testcases/bpel2owfn/sa_tests/sa00067.bpel", "67"},
                {"Testcases/bpel2owfn/sa_tests/sa00068.bpel", "68"},
                {"Testcases/bpel2owfn/sa_tests/sa00069.bpel", "69"},
                {"Testcases/bpel2owfn/sa_tests/sa00070.bpel", "70"},
                {"Testcases/bpel2owfn/sa_tests/sa00071.bpel", "71"},
                {"Testcases/bpel2owfn/sa_tests/sa00072.bpel", "72"},
                {"Testcases/bpel2owfn/sa_tests/sa00073.bpel", "73"},
                {"Testcases/bpel2owfn/sa_tests/sa00074.bpel", "74"},
                {"Testcases/bpel2owfn/sa_tests/sa00075.bpel", "75"},
                {"Testcases/bpel2owfn/sa_tests/sa00076.bpel", "76"},
                {"Testcases/bpel2owfn/sa_tests/sa00077.bpel", "77"},
                {"Testcases/bpel2owfn/sa_tests/sa00078.bpel", "78"},
                {"Testcases/bpel2owfn/sa_tests/sa00079.bpel", "79"},
                {"Testcases/bpel2owfn/sa_tests/sa00080.bpel", "80"},
                {"Testcases/bpel2owfn/sa_tests/sa00081.bpel", "81"},
                {"Testcases/bpel2owfn/sa_tests/sa00082.bpel", "82"},
                {"Testcases/bpel2owfn/sa_tests/sa00083.bpel", "83"},
                {"Testcases/bpel2owfn/sa_tests/sa00084.bpel", "84"},
                {"Testcases/bpel2owfn/sa_tests/sa00087.bpel", "87"},
                {"Testcases/bpel2owfn/sa_tests/sa00088.bpel", "88"},
                {"Testcases/bpel2owfn/sa_tests/sa00091.bpel", "91"},
                {"Testcases/bpel2owfn/sa_tests/sa00092.bpel", "92"},
                {"Testcases/bpel2owfn/sa_tests/sa00093.bpel", "93"}

        };
        return Arrays.asList(data);
    }

    @Override
	@Test @Ignore
    public void testValidators() throws Exception {
        ValidationResult validationResult = BpelLint.buildWithoutSchemaValidation().validate(Paths.get(bpel));

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        new ValidationResultPrinter(ps).printResults(VerbosityLevel.NORMAL,
                validationResult);
        String data = "\n" + baos.toString() + "\n";

        assertEquals("BPEL: " + bpel + data, violatedRules,
                validationResult.getViolatedRules());
    }

}
