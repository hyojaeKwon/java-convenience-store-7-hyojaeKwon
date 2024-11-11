package store.user.controller.handler.retry;

public class RetryHandler {

    private static final String PREFIX = "[ERROR] ";
    private static final String RETRY = " 다시 입력해 주세요.";

    private RetryHandler() {
    }

    public static <T> T retry(RetryableAction<T> action) {
        while (true) {
            try {
                return action.execute();
            } catch (IllegalArgumentException e) {
                printMessage(e.getMessage());
            }
        }
    }

    private static void printMessage(String msg) {
        System.out.println(PREFIX + msg + RETRY);
    }
}
