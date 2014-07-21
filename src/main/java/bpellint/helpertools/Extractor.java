package bpellint.helpertools;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.ParsingException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Extractor {
    private static final Builder builder = new Builder();
    private static final Set<String> results = new HashSet<>();
    private static final List<String> coupling = new LinkedList<>();

    public static void main(String[] args) throws Exception {
        Path path = Paths.get("Testcases/rules");

        if (!Files.isDirectory(path)) {
            System.err.println("This path " + path + "is not a directory.");
        } else {
            traverse(path);
            print();
        }
    }

    private static void traverse(Path path) throws ParsingException, IOException {
        if (Files.isDirectory(path)) {
            for (File file : path.toFile().listFiles()) {
                traverse(file.toPath());
            }
        }
        if (Files.isRegularFile(path) && path.toString().endsWith(".bpel")) {
            Document document = builder.build(path.toFile());
            String betsyTestName = document.getRootElement().getAttribute("name")
                    .getValue();
            results.add(betsyTestName);
            coupling.add(path.toString() + "," + betsyTestName);
        }
    }

    private static void print() {
        for (String saTestCase : coupling) {
            System.out.println(saTestCase);
        }
        for (String betsyTestName : results) {
            System.out.println(betsyTestName);
        }

    }
}
