package isabel;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

class HappyPathTests {

    private final Path baseDir;

    public HappyPathTests(Path baseDir) {
        this.baseDir = Objects.requireNonNull(baseDir, "baseDir must not be null");
    }

    private List<Object[]> aggregateBpelFiles(Path folder) throws IOException {

        List<Object[]> result = new LinkedList<>();

        try (DirectoryStream<Path> fileStream = Files.newDirectoryStream(folder, "*.bpel")) {
            for (Path path : fileStream) {
                if (Files.isDirectory(path)) {
                    // recursion
                    result.addAll(aggregateBpelFiles(path));
                } else if (Files.isRegularFile(path)) {
                    result.add(new Object[]{path.toString(), ""});
                }
            }
        }

        return result;
    }

    List<Object[]> list() throws IOException {
        return Collections.unmodifiableList(aggregateBpelFiles(baseDir));
    }

}
