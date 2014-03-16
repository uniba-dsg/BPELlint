package isabel;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BPELProcessForgotten {

    public static void main(String[] args) throws IOException {
        List<Path> existingBpelFiles = getExistingBpelProcesses();
        Set<Path> usedBpelFiles = getFunctionalBpelProcesses();
        usedBpelFiles.addAll(getXsdSchemaBpelProcesses());

        existingBpelFiles.removeAll(usedBpelFiles);

        for (Path path : existingBpelFiles) {
            System.err.println(path);
        }
    }

    private static List<Path> getExistingBpelProcesses() throws IOException {
        return FileUtils.getBpelFiles(Paths.get("Testcases/rules"));
    }

    private static Set<Path> getFunctionalBpelProcesses() {
        return getPathsFromData(FunctionalValidatorTests.saViolationTests());
    }

    private static Set<Path> getXsdSchemaBpelProcesses() {
        return getPathsFromData(XsdSchemaValidatorTests.data());
    }

    private static Set<Path> getPathsFromData(Collection<Object[]> data1) {
        Set<Path> usedBpelFiles = new HashSet<>();
        for (Object[] data : data1) {
            Path path = Paths.get(data[0].toString());
			boolean unique = usedBpelFiles.add(path);
            if (!unique) {
            	System.out.println("Waring: Used again" + path);
			}
        }
        return usedBpelFiles;
    }
}
