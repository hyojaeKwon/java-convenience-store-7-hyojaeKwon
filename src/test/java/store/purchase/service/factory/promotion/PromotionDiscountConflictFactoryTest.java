package store.purchase.service.factory.promotion;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.purchase.controller.dto.request.purchase.PurchaseItemRequest;
import store.purchase.controller.dto.response.PromotionConflictResponse;
import store.purchase.domain.Purchase;
import store.purchase.domain.PurchaseCreate;
import store.purchase.domain.discount.promotion.PromotionDiscount;
import store.purchase.domain.discount.promotion.PromotionDiscountInfo;
import store.purchase.domain.discount.promotion.PromotionDiscountItem;

class PromotionDiscountConflictFactoryTest {

    @Test
    @DisplayName("PromotionDiscountItem이 포함된 Purchase 객체로 PromotionConflictResponse가 정상적으로 생성된다")
    void testGetPromotionConflicts() {
        // given
        PromotionDiscountItem item1 = new PromotionDiscountItem("item1", 100L, 10, 5);
        PromotionDiscountItem item2 = new PromotionDiscountItem("item2", 200L, 5, 3);

        PromotionDiscountInfo info1 = PromotionDiscountInfo.create(item1);
        PromotionDiscountInfo info2 = PromotionDiscountInfo.createCanGetMore(item2, 2);

        PromotionDiscount promotionDiscount = new PromotionDiscount(List.of(info1, info2));

        PurchaseCreate purchaseCreate1 = PurchaseCreate.create(new PurchaseItemRequest("item1", 10), 100, true);
        PurchaseCreate purchaseCreate2 = PurchaseCreate.create(new PurchaseItemRequest("item2", 5), 200, true);

        Purchase purchase = Purchase.createPurchase(List.of(purchaseCreate1, purchaseCreate2), "12345");
        purchase = purchase.applyPromotionDiscount(promotionDiscount);

        // when
        PromotionDiscountConflictFactory factory = new PromotionDiscountConflictFactory();
        PromotionConflictResponse response = factory.getPromotionConflicts(purchase);

        Assertions.assertThat(response.getConflictList()).hasSize(2);
        Assertions.assertThat(response.getConflictList().get(0).getName()).isEqualTo("item1");
        Assertions.assertThat(response.getConflictList().get(1).getName()).isEqualTo("item2");
    }

    @Test
    @DisplayName("PromotionDiscountInfo가 없는 경우 빈 PromotionConflictResponse가 생성된다")
    void testGetPromotionConflictsWithNoDiscounts() {
        // given
        PromotionDiscount promotionDiscount = new PromotionDiscount(List.of());

        PurchaseCreate purchaseCreate1 = PurchaseCreate.create(new PurchaseItemRequest("item1", 10), 100, true);
        PurchaseCreate purchaseCreate2 = PurchaseCreate.create(new PurchaseItemRequest("item2", 5), 200, true);

        Purchase purchase = Purchase.createPurchase(List.of(purchaseCreate1, purchaseCreate2), "12345");
        purchase = purchase.applyPromotionDiscount(promotionDiscount);

        // when
        PromotionDiscountConflictFactory factory = new PromotionDiscountConflictFactory();
        PromotionConflictResponse response = factory.getPromotionConflicts(purchase);

        Assertions.assertThat(response.getConflictList()).isEmpty();
    }
}