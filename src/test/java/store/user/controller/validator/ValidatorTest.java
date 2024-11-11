package store.user.controller.validator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.common.exception.input.InputException;

class ValidatorTest {

    @Test
    @DisplayName("입력이 null 또는 빈 문자열일 경우 예외를 발생시킨다")
    void testValidateStringWithBlankInput() {
        String input = "";

        Assertions.assertThatThrownBy(() -> Validator.validateString(input)).isInstanceOf(InputException.class);
    }

}