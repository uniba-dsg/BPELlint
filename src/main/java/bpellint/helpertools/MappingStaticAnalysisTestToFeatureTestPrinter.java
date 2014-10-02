package bpellint.helpertools;

import nu.xom.Builder;
import nu.xom.Document;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

/**
 * Mapping static-analysis-test to feature-test
 */
public class MappingStaticAnalysisTestToFeatureTestPrinter {

    private static final Builder builder = new Builder();
    private static final List<String> coupling = new LinkedList<>();
    public static final Path PATH = Paths.get("Testcases/rules");

    public static void main(String[] args) throws Exception {

        if (!Files.isDirectory(PATH)) {
            System.err.println("This path " + PATH + "is not a directory.");
        } else {
            traverse(PATH);
            print();
        }
    }

    private static void traverse(Path path) throws Exception {
        if (Files.isDirectory(path)) {
            for (File file : path.toFile().listFiles()) {
                traverse(file.toPath());
            }
        }
        if (Files.isRegularFile(path) && path.toString().endsWith(".bpel")) {
            Document document = builder.build(path.toFile());
            String betsyTestName = document.getRootElement().getAttribute("name")
                    .getValue();
            String faultTestName = PATH.relativize(path).toString().replaceAll("\\.bpel", "").replaceAll("\\\\", "-");

            if (!faultTestName.contains("IGNORE_ME")) {
                coupling.add(faultTestName + ";" + betsyTestName);
            }
        }
    }

    private static void print() {
        System.out.println("Count: " + coupling.size());

        System.out.println("fault-test;correct-test"); // legend
        for (String saTestCase : coupling) {
            System.out.println(saTestCase);
        }
    }
}
