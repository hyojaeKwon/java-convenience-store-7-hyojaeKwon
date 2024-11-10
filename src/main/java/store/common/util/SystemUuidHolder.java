package store.common.util;

import java.util.UUID;

public class SystemUuidHolder implements IdHolder {
    @Override
    public String generateId() {
        return UUID.randomUUID().toString();
    }
}
