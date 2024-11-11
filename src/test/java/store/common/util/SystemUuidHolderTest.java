package store.common.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SystemUuidHolderTest {

    private IdHolder idHolder;

    @BeforeEach
    void setUp() {
        idHolder = new SystemUuidHolder();
    }

    @Test
    @DisplayName("UUID를 생성하는지 확인한다.")
    void generateUUIDtest() {
        String id = idHolder.generateId();
        Assertions.assertThat(id)
                .matches("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");
    }
}