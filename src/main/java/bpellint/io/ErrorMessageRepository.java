package bpellint.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ErrorMessageRepository {

    public String getShort(String saNumber, int type)
            throws DescriptionNotFoundException {
        try {
            String content = getDescriptionFileContent(saNumber);
            return content.split("\n")[type - 1].trim();
        } catch (IOException | ArrayIndexOutOfBoundsException e) {
            throw new DescriptionNotFoundException(
                    "Short description for rule " + saNumber
                            + " could not be found.");
        }
    }

    public String getLong(String saNumber) throws DescriptionNotFoundException {
        try {
            String content = getDescriptionFileContent(saNumber);
            return content.split("@@@")[1].trim();
        } catch (IOException | ArrayIndexOutOfBoundsException e) {
            throw new DescriptionNotFoundException("Short description "
                    + saNumber + " could not be found.");
        }
    }

    private String getDescriptionFileContent(String saNumber) throws IOException {
        return new String(Files.readAllBytes(getFilePath(saNumber)));
    }

    private Path getFilePath(String saNumber) {
        return Paths.get("Testcases").resolve("rules").resolve(saNumber).resolve(saNumber + ".txt");
    }

}
