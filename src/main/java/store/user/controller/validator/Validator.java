package store.user.controller.validator;

public class Validator {

    private Validator() {
    }

    public static void validateString(String input) {
        if (input == null || input.isEmpty()) {
            throw new IllegalArgumentException("input이 null이 될 수 없습니다");
        }
    }

    public static void validateControl(String input) {
        if (input == null || input.isEmpty()) {
            throw new IllegalArgumentException("input이 null이 될 수 없습니다");
        }
        if (!input.equals("Y") && !input.equals("N")) {
            throw new IllegalArgumentException("Y / N 이여야 함");
        }
    }
}
