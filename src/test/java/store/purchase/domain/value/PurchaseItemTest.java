package store.purchase.domain.value;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.purchase.controller.dto.request.purchase.PurchaseItemRequest;
import store.purchase.domain.PurchaseCreate;

class PurchaseItemTest {

    @Test
    @DisplayName("PurchaseCreate 객체를 통해 PurchaseItem이 정상적으로 생성된다.")
    void testCreatePurchaseItem() {
        PurchaseCreate purchaseCreate = PurchaseCreate.create(new PurchaseItemRequest("item1", 10), 100L, true);

        PurchaseItem purchaseItem = PurchaseItem.create(purchaseCreate);

        Assertions.assertThat(purchaseItem.getName()).isEqualTo("item1");
        Assertions.assertThat(purchaseItem.isPromotion()).isTrue();
        Assertions.assertThat(purchaseItem.getAmount()).isEqualTo(10L);
        Assertions.assertThat(purchaseItem.getPrice()).isEqualTo(100L);
        Assertions.assertThat(purchaseItem.getTotalPrice()).isEqualTo(1000L);
    }

    @Test
    @DisplayName("추가 구매 수량이 더해지며 총 금액 계산된다.")
    void testAddPurchaseAmount() {
        PurchaseCreate purchaseCreate = PurchaseCreate.create(new PurchaseItemRequest("item2", 5), 200L, false);
        PurchaseItem purchaseItem = PurchaseItem.create(purchaseCreate);

        PurchaseItem updatedPurchaseItem = purchaseItem.addPurchaseAmount(3);

        Assertions.assertThat(updatedPurchaseItem.getAmount()).isEqualTo(8L);
        Assertions.assertThat(updatedPurchaseItem.getTotalPrice()).isEqualTo(1600L);
    }

}