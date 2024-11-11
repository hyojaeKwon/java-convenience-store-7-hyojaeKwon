package store.purchase.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.purchase.controller.dto.request.purchase.PurchaseItemRequest;

class PurchaseCreateTest {

    @Test
    @DisplayName("정상적인 객체를 생성한다.")
    void testCreatePurchase() {
        PurchaseItemRequest request = new PurchaseItemRequest("item", 10);  // name: "item", quantity: 10
        long price = 100L;
        boolean isPromotion = true;

        PurchaseCreate purchaseCreate = PurchaseCreate.create(request, price, isPromotion);

        Assertions.assertThat(purchaseCreate.getName()).isEqualTo("item");
        Assertions.assertThat(purchaseCreate.getQuantity()).isEqualTo(10L);
        Assertions.assertThat(purchaseCreate.getPrice()).isEqualTo(100L);
        Assertions.assertThat(purchaseCreate.isPromotion()).isTrue();
    }

    @Test
    @DisplayName("수량이 0 이하일 때 예외를 반환한다.")
    void testCreateWithNegativeQuantity() {
        PurchaseItemRequest request = new PurchaseItemRequest("item", -5);
        long price = 100L;
        boolean isPromotion = false;

        Assertions.assertThatThrownBy(() -> PurchaseCreate.create(request, price, isPromotion))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("가격이 음수일 때 예외를 반환한다.")
    void testCreateExceptionWhenNegativePrice() {
        PurchaseItemRequest request = new PurchaseItemRequest("item", 10);
        long price = -100L;
        boolean isPromotion = false;

        Assertions.assertThatThrownBy(() -> PurchaseCreate.create(request, price, isPromotion))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("프로모션 아이템이 아닌 경우 객체 생성한다.")
    void testCreateGeneralItem() {
        PurchaseItemRequest request = new PurchaseItemRequest("nonPromoItem", 5);  // name: "nonPromoItem", quantity: 5
        long price = 200L;
        boolean isPromotion = false;

        PurchaseCreate purchaseCreate = PurchaseCreate.create(request, price, isPromotion);

        Assertions.assertThat(purchaseCreate.getName()).isEqualTo("nonPromoItem");
        Assertions.assertThat(purchaseCreate.getQuantity()).isEqualTo(5L);
        Assertions.assertThat(purchaseCreate.getPrice()).isEqualTo(200L);
        Assertions.assertThat(purchaseCreate.isPromotion()).isFalse();
    }
}