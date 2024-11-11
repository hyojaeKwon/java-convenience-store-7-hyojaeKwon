package store.user.controller.handler.retry;

public interface RetryableAction<T> {
    T execute() throws IllegalArgumentException;
}
