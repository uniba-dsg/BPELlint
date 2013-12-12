package isabel;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class BPELProcessForgotten {

    public static void main(String[] args) throws IOException {
        List<Path> existingBpelFiles = getExistingBpelProcesses();
        List<Path> usedBpelFiles = getUsedBpelProcesses();

        existingBpelFiles.removeAll(usedBpelFiles);

        for (Path path : existingBpelFiles) {
            System.err.println(path);
        }
    }

    private static List<Path> getExistingBpelProcesses() throws IOException {
        return FileUtils.getBpelFiles(Paths.get("Testcases/rules"));
    }

    private static List<Path> getUsedBpelProcesses() {
        Collection<Object[]> datas = FunctionalValidatorTests.saViolationTests();
        List<Path> usedBpelFiles = new LinkedList<>();
        for (Object[] data : datas) {
            usedBpelFiles.add(Paths.get(data[0].toString()));
        }
        return usedBpelFiles;
    }
}
