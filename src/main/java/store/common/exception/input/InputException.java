package store.common.exception.input;

public class InputException extends IllegalArgumentException {
    public static final String NOT_VALID_INPUT = "올바르지 않은 형식으로 입력했습니다.";
    public static final String NOT_VALID_CONTROL = "잘못된 입력입니다.";

    public static final String NOT_VALID_PARSE = "잘못된 입력입니다.";
    public static final String NOT_VALID_DATE = "잘못된 날짜 입력입니다.";

    public static final String BLANK_STRING_INPUT = "입력값이 비었습니다.";
    public InputException(String s) {
        super(s);
    }
}
