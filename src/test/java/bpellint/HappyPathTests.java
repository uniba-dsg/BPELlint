package bpellint;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

class HappyPathTests {

    private final Path baseDir;

    HappyPathTests(Path baseDir) {
        this.baseDir = Objects.requireNonNull(baseDir, "baseDir must not be null");
    }

    private List<Object[]> createTestFromPath(List<Path> paths) {
        List<Object[]> result = new LinkedList<>();

        for (Path path : paths) {
            result.add(new Object[]{path.toString(), ""});
        }

        return result;
    }

    List<Object[]> list() throws IOException {
        return Collections.unmodifiableList(createTestFromPath(FileUtils.getBpelFiles(baseDir)));
    }

}
