package store.user.controller.input.reader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.user.controller.validator.Validator;

class InputFileReaderTest {

    @Test
    @DisplayName("빈 파일을 읽을 때 예외가 발생한다")
    void testEmptyFileThrowsException() throws IOException {
        Path tempFile = Files.createTempFile("empty", ".md");
        Files.writeString(tempFile, "");

        Assertions.assertThatThrownBy(() -> Validator.validateString(Files.readString(tempFile)))
                .isInstanceOf(IllegalArgumentException.class);

        Files.delete(tempFile);
    }

    @Test
    @DisplayName("잘못된 파일 형식을 읽어 null 문제가 생기면 예외가 발생한다")
    void testInvalidFileThrowsException() {
        String invalidContent = null;

        Assertions.assertThatThrownBy(() -> Validator.validateString(invalidContent))
                .isInstanceOf(IllegalArgumentException.class);
    }


}