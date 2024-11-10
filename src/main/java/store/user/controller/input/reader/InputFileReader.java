package store.user.controller.input.reader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import store.user.controller.validator.Validator;

public class InputFileReader {

    private static final String PRODUCT = "products.md";
    private static final String PROMOTION = "promotions.md";

    public List<String> readRule() {
        try {
            return readFile(PROMOTION);
        } catch (IOException | URISyntaxException e) {
            throw new IllegalStateException("Could not read rule", e);
        }
    }

    public List<String> readItem() {
        try {
            return readFile(PRODUCT);
        } catch (IOException | URISyntaxException e) {
            throw new IllegalStateException("Could not read item", e);
        }
    }

    private List<String> readFile(String fileName) throws IOException, URISyntaxException {
        Path path = Paths.get(Objects.requireNonNull(getClass().getClassLoader().getResource(fileName)).toURI());
        String rawFileInput = Files.readString(path);
        Validator.validateString(rawFileInput);
        return separateLines(rawFileInput);
    }


    private List<String> separateLines(String rawString) {
        List<String> result = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new StringReader(rawString))) {
            String line = br.readLine();
            while (line != null) {
                result.add(line);
                line = br.readLine();
            }
        } catch (IOException e) {
            throw new IllegalStateException("Could not read line", e);
        }
        return result;
    }

}
