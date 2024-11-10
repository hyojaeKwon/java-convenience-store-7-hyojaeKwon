package store.purchase.controller.dto;

public class GeneralTransfer<T> {
    private final String userId;
    private final T context;

    public GeneralTransfer(String userId, T context) {
        if (userId.isBlank() || userId.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.userId = userId;
        this.context = context;
    }

    public String getUserId() {
        return userId;
    }

    public T getContext() {
        return context;
    }
}
