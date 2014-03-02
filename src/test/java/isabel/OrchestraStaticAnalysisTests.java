package isabel;

import isabel.io.ValidationResultPrinter;
import isabel.io.VerbosityLevel;
import isabel.tool.Isabel;
import isabel.tool.ValidationResult;
import org.junit.Test;
import org.junit.runners.Parameterized;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Paths;
import java.util.Arrays;
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
        bpelFiles.addAll(OrchestraStaticAnalysisTests.orchestraSATests());

        return bpelFiles;
    }

    public static List<Object[]> orchestraSATests() {
        Object[][] data = new Object[][]{

                {"Testcases/orchestra/sa00001/sa00001_1.bpel", "1"},
                {"Testcases/orchestra/sa00001/sa00001_2.bpel", "1"},
                {"Testcases/orchestra/sa00001/sa00001_3.bpel", ""}, // seems to be a positive example, thus no error

                {"Testcases/orchestra/sa00002/sa00002.bpel", "2"},

                {"Testcases/orchestra/sa00003/sa00003_1.bpel", "3"},
                {"Testcases/orchestra/sa00003/sa00003_2.bpel", "3"},
                {"Testcases/orchestra/sa00003/sa00003_3.bpel", "3"},

                // TODO cannot be checked yet, engine dependent rule is not implemented
//                {"Testcases/orchestra/sa00004/sa00004_1.bpel", "4"},
//                {"Testcases/orchestra/sa00004/sa00004_10.bpel", "4"},
//                {"Testcases/orchestra/sa00004/sa00004_11.bpel", "4"},
//                {"Testcases/orchestra/sa00004/sa00004_12.bpel", "4"},
//                {"Testcases/orchestra/sa00004/sa00004_13.bpel", "4"},
//                {"Testcases/orchestra/sa00004/sa00004_14.bpel", "4"},
//                {"Testcases/orchestra/sa00004/sa00004_15.bpel", "4"},
//                {"Testcases/orchestra/sa00004/sa00004_16.bpel", "4"},
//                {"Testcases/orchestra/sa00004/sa00004_17.bpel", "4"},
//                {"Testcases/orchestra/sa00004/sa00004_18.bpel", "4"},
//                {"Testcases/orchestra/sa00004/sa00004_19.bpel", "4"},
//                {"Testcases/orchestra/sa00004/sa00004_2.bpel", "4"},
//                {"Testcases/orchestra/sa00004/sa00004_20.bpel", "4"},
//                {"Testcases/orchestra/sa00004/sa00004_21.bpel", "4"},
//                {"Testcases/orchestra/sa00004/sa00004_22.bpel", "4"},
//                {"Testcases/orchestra/sa00004/sa00004_23.bpel", "4"},
//                {"Testcases/orchestra/sa00004/sa00004_3.bpel", "4"},
//                {"Testcases/orchestra/sa00004/sa00004_4.bpel", "4"},
//                {"Testcases/orchestra/sa00004/sa00004_5.bpel", "4"},
//                {"Testcases/orchestra/sa00004/sa00004_6.bpel", "4"},
//                {"Testcases/orchestra/sa00004/sa00004_7.bpel", "4"},
//                {"Testcases/orchestra/sa00004/sa00004_8.bpel", "4"},
//                {"Testcases/orchestra/sa00004/sa00004_9.bpel", "4"},

                {"Testcases/orchestra/sa00005/sa00005.bpel", "5"},

                {"Testcases/orchestra/sa00006/sa00006.bpel", "6"},

                {"Testcases/orchestra/sa00007/sa00007.bpel", "8"}, // this rule is obviously SA#8 the name was switched

                {"Testcases/orchestra/sa00008/sa00008.bpel", "7, 77"}, // this rule is obviously SA#7 the name was switched, the target is not present, thus SA#77 is violated as well

                {"Testcases/orchestra/sa00015/sa00015.bpel", "15"},

                {"Testcases/orchestra/sa00016/sa00016.bpel", "16, 10"}, // without a role the message activity portType cannot be determined

                {"Testcases/orchestra/sa00017/sa00017.bpel", "17"},

                {"Testcases/orchestra/sa00018/sa00018.bpel", "18"},

                {"Testcases/orchestra/sa00023/sa00023.bpel", "23"},

                {"Testcases/orchestra/sa00025/sa00025.bpel", "25"},

                {"Testcases/orchestra/sa00044/sa00044.bpel", "44"},

                {"Testcases/orchestra/sa00051/sa00051.bpel", "51, 50, 54"}, // Bad construction. Message has two parts, none of them but another part is referenced in toParts

                {"Testcases/orchestra/sa00052/sa00052.bpel", "52, 53"}, // Bad construction. Message has a different part than referenced in toParts

                {"Testcases/orchestra/sa00055/sa00055.bpel", "55, 53, 58, 10"}, // Bad construction. No variable intType defined, neither a part of the message is defined with this name, and the other parts of the message are not referenced. The variable definition have to be messageType not type.

                {"Testcases/orchestra/sa00056/sa00056.bpel", "56"},
                {"Testcases/orchestra/sa00056/sa00056_2.bpel", "56"},
                {"Testcases/orchestra/sa00056/sa00056_3.bpel", ""}, // Valid process
                {"Testcases/orchestra/sa00056/sa00056_4.bpel", "56"},
                {"Testcases/orchestra/sa00056/sa00056_5.bpel", ""}, // Valid process

                {"Testcases/orchestra/sa00057/sa00057.bpel", "57"},
                {"Testcases/orchestra/sa00057/sa00057_2.bpel", "57"},
                {"Testcases/orchestra/sa00057/sa00057_3.bpel", "57"}, // TODO modify result or implementation according to Simon's feedback
                {"Testcases/orchestra/sa00057/sa00057_4.bpel", "57"},

                {"Testcases/orchestra/sa00059/sa00059.bpel", "59"},

                {"Testcases/orchestra/sa00064/sa00064.bpel", "64"},

                {"Testcases/orchestra/sa00065/sa00065.bpel", "65, 66, 56"}, // Rules 65 and 66 are overlapping, if not an additional source/target is added. Rules 65 and 56 are overlapping, if the start activity is part of the linked graph, with missing <link> definition.

                {"Testcases/orchestra/sa00066/sa00066.bpel", "66, 72"}, // Bad construction. Twice double linked to self and next, creating three cycles.

                {"Testcases/orchestra/sa00068/sa00068.bpel", "68"},

                {"Testcases/orchestra/sa00069/sa00069.bpel", "69"},

                // TODO not yet implemented, expression parsing
//                {"Testcases/orchestra/sa00074/sa00074.bpel", "74"},
//                {"Testcases/orchestra/sa00074/sa00074_2.bpel", "74"},
//                {"Testcases/orchestra/sa00074/sa00074_3.bpel", "74"},

                // TODO not yet implemented, expression parsing
//                {"Testcases/orchestra/sa00075/sa00075.bpel", "75"},

                {"Testcases/orchestra/sa00076/sa00076.bpel", "76"},

                {"Testcases/orchestra/sa00080/sa00080.bpel", "80"},

                {"Testcases/orchestra/sa00081/sa00081_1.bpel", "81"},
                {"Testcases/orchestra/sa00081/sa00081_2.bpel", "81, 10"}, // "QName" is not defined anywhere
                {"Testcases/orchestra/sa00081/sa00081_3.bpel", "81, 10"}, // "QName" is not defined anywhere
                {"Testcases/orchestra/sa00081/sa00081_4.bpel", "81, 10"}, // "QName" is not defined anywhere
                {"Testcases/orchestra/sa00081/sa00081_5.bpel", "81, 10"}, // "QName" is not defined anywhere

                {"Testcases/orchestra/sa00083/sa00083.bpel", "83"},

                {"Testcases/orchestra/sa00084/sa00084.bpel", "84, 10, 90"}, // operation cannot be resolved without partnerLink and variable requires a type
                {"Testcases/orchestra/sa00084/sa00084_2.bpel", ""}, // valid process

                {"Testcases/orchestra/sa00085/sa00085.bpel", "85, 47"}, // Bad construction. receive without variable assignment for non empty message.
                {"Testcases/orchestra/sa00085/sa00085_2.bpel", "85, 47"}, // Bad construction. receive without variable assignment for non empty message.
                {"Testcases/orchestra/sa00085/sa00085_3.bpel", "85, 47"}, // Bad construction. receive without variable assignment for non empty message.
                {"Testcases/orchestra/sa00085/sa00085_4.bpel", "85, 47"}, // Bad construction. receive without variable assignment for non empty message.

                {"Testcases/orchestra/sa00086/sa00086.bpel", "86, 47"}, // Bad construction. receive without variable assignment for non empty message.
                {"Testcases/orchestra/sa00086/sa00086_2.bpel", "86, 47"}, // Bad construction. receive without variable assignment for non empty message.

                {"Testcases/orchestra/sa00087/sa00087.bpel", "87, 47"}, // Bad construction. receive without variable assignment for non empty message.
                {"Testcases/orchestra/sa00087/sa00087_2.bpel", "47"}, // Bad construction. receive without variable assignment for non empty message. messageType equals <message> name
                {"Testcases/orchestra/sa00087/sa00087_3.bpel", "47"}, // Bad construction. receive without variable assignment for non empty message. element equals <part> element
                {"Testcases/orchestra/sa00087/sa00087_4.bpel", "87, 47"}, // Bad construction. receive without variable assignment for non empty message.
                {"Testcases/orchestra/sa00087/sa00087_5.bpel", "87, 47"}, // Bad construction. receive without variable assignment for non empty message.

                {"Testcases/orchestra/sa00088/sa00088.bpel", "88, 47"}, // Bad construction. receive without variable assignment for non empty message.
                {"Testcases/orchestra/sa00088/sa00088_2.bpel", "45, 47"}, // Bad construction. property has no type from the XSD-Schema, thus can not be a simpleType. receive without variable assignment for non empty message.

                {"Testcases/orchestra/sa00089/sa00089.bpel", "89, 61"}, // SA#89 is included in SA#61
                {"Testcases/orchestra/sa00089/sa00089_2.bpel", "61"}, // SA#89 is included in SA#61. reply activity is missing, but the onEvent attribute could resolve successfully.

                {"Testcases/orchestra/sa00090/sa00090.bpel", "90, 47"}, // Bad construction. receive without variable assignment for non empty message.
                {"Testcases/orchestra/sa00090/sa00090_2.bpel", "47"}, // Bad construction. receive without variable assignment for non empty message. messageType is defined as required
                {"Testcases/orchestra/sa00090/sa00090_3.bpel", "47"}, // Bad construction. receive without variable assignment for non empty message. element is defined as required
                {"Testcases/orchestra/sa00090/sa00090_4.bpel", "90, 47"}, // Bad construction. receive without variable assignment for non empty message.

                {"Testcases/orchestra/sa00092/sa00092.bpel", "92"},

                {"Testcases/orchestra/sa00093/sa00093_1.bpel", "93"},
                {"Testcases/orchestra/sa00093/sa00093_2.bpel", "93, 10"}, // "QName" is not defined anywhere as element
                {"Testcases/orchestra/sa00093/sa00093_3.bpel", "93, 10"}, // "QName" is not defined anywhere as messageType

        };
        return Arrays.asList(data);
    }

    @Override
	@Test
    public void testValidators() throws Exception {
        ValidationResult validationResult = Isabel.buildWithoutSchemaValidation().validate(Paths.get(bpel));

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        new ValidationResultPrinter(ps).printResults(VerbosityLevel.NORMAL,
                validationResult);
        String data = "\n" + baos.toString() + "\n";

        assertEquals("BPEL: " + bpel + data, violatedRules,
                validationResult.getViolatedRules());
    }

}
