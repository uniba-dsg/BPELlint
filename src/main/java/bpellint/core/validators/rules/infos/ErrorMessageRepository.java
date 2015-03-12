package bpellint.core.validators.rules.infos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

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

    private String getDescriptionFileContent(String saNumber) throws IOException, DescriptionNotFoundException {
        InputStream stream = getClass().getResourceAsStream("/ruleDescriptions/" + saNumber + ".txt");
        if(stream == null) {
            throw new DescriptionNotFoundException("Could not find txt file");
        }

        StringBuilder result = new StringBuilder();
        try(Scanner reader = new Scanner(stream)) {
            while(reader.hasNextLine()) {
                result.append(reader.nextLine());
                result.append("\n");
            }
        }

        return result.toString();
    }

}
