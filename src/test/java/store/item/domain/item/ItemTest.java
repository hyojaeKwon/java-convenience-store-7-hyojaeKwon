package store.item.domain.item;

import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.common.util.IdHolder;
import store.common.util.SystemUuidHolder;
import store.item.controller.dto.request.ItemRequest;

class ItemTest {

    private IdHolder idHolder;
    private ItemRequest mockItemRequest;

    @BeforeEach
    void setUp() {
        idHolder = new SystemUuidHolder();
        mockItemRequest = new ItemRequest("item", 3000, 4, Optional.empty());
    }

    @Test
    @DisplayName("Item 객체를 static factory method를 이용하여 생성한다.")
    void createItemTest() {
        Item item = Item.create(idHolder, mockItemRequest);

        Assertions.assertThat(item.getId()).isInstanceOf(String.class).isNotNull();
        Assertions.assertThat(item.getName()).isEqualTo(mockItemRequest.getName());
        Assertions.assertThat(item.getPrice()).isEqualTo(mockItemRequest.getPrice());
        Assertions.assertThat(item.getStockQuantity()).isEqualTo(mockItemRequest.getQuantity());
    }

    @Test
    @DisplayName("createMock 메서드를 이용해 Mock Item 객체를 생성한다.")
    void createMockItemTest() {
        String itemName = "mockItem";
        long price = 1500;

        Item mockItem = Item.createMock(idHolder, itemName, price);

        Assertions.assertThat(mockItem.getId()).isInstanceOf(String.class).isNotNull();
        Assertions.assertThat(mockItem.getName()).isEqualTo(itemName);
        Assertions.assertThat(mockItem.getPrice()).isEqualTo(price);
        Assertions.assertThat(mockItem.getStockQuantity()).isZero(); // mockItem의 재고는 0이어야 함
    }

    @Test
    @DisplayName("구매 시 지정된 수량만큼 재고가 줄어든 새로운 Item 객체를 반환한다.")
    void purchaseItemTest() {
        Item item = Item.create(idHolder, mockItemRequest);
        long purchaseQuantity = 2;

        Item purchasedItem = item.purchase(purchaseQuantity);

        Assertions.assertThat(purchasedItem.getStockQuantity()).isEqualTo(item.getStockQuantity() - purchaseQuantity);
    }

    @Test
    @DisplayName("음수 수량으로 구매하려고 하면 예외가 발생한다.")
    void purchaseNegativeQuantityTest() {
        Item item = Item.create(idHolder, mockItemRequest);

        long negativeQuantity = -1;

        Assertions.assertThatThrownBy(() -> item.purchase(negativeQuantity))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("재고보다 많은 수량으로 구매하려고 하면 예외가 발생한다.")
    void purchaseExceedQuantityTest() {
        Item item = Item.create(idHolder, mockItemRequest);

        long exceedQuantity = item.getStockQuantity() + 1;

        Assertions.assertThatThrownBy(() -> item.purchase(exceedQuantity)).isInstanceOf(IllegalArgumentException.class);
    }
}