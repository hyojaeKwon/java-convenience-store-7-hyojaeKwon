package store.user.controller.validator;

import static store.common.exception.input.InputException.BLANK_STRING_INPUT;

import store.common.exception.input.InputException;

public class Validator {

    private Validator() {
    }

    public static void validateString(String input) {
        if (input == null || input.isEmpty()) {
            throw new InputException(BLANK_STRING_INPUT);
        }
    }
}
