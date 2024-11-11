package store.purchase.domain;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.purchase.controller.dto.request.purchase.PurchaseItemRequest;
import store.purchase.domain.discount.membership.MembershipDiscount;
import store.purchase.domain.discount.promotion.PromotionDiscount;
import store.purchase.domain.discount.promotion.PromotionDiscountInfo;
import store.purchase.domain.discount.promotion.PromotionDiscountItem;
import store.purchase.domain.value.PurchaseItem;
import store.purchase.domain.value.PurchaseItems;

class PurchaseTest {

    private Purchase purchase;
    private PurchaseCreate purchaseCreate;
    private PurchaseItemRequest request;

    @BeforeEach
    void setUp() {
        PurchaseItemRequest request = new PurchaseItemRequest("item", 10);
        long price = 100L;
        boolean isPromotion = true;
        String id = "12345";
        purchaseCreate = PurchaseCreate.create(request, price, isPromotion);
        List<PurchaseCreate> purchaseCreates = List.of(purchaseCreate);

        purchase = Purchase.createPurchase(purchaseCreates, id);
    }

    @Test
    @DisplayName("정상적인 요청으로 Purchase 객체 생성 성공")
    void testCreatePurchase() {
        Assertions.assertThat(purchase.getId()).isEqualTo("12345");
        Assertions.assertThat(purchase.getPurchaseItems()).hasSize(1);
        Assertions.assertThat(purchase.getPurchaseSum()).isEqualTo(1000L);
        Assertions.assertThat(purchase.getPurchaseItems().get(0).getName()).isEqualTo("item");
    }

    @Test
    @DisplayName("프로모션 할인 적용 시 할인 상태가 CONFLICTED로 설정된다")
    void testApplyPromotionDiscount() {
        PurchaseItems items = new PurchaseItems(List.of(PurchaseItem.create(purchaseCreate)));
        PromotionDiscountItem promotionDiscountItem = new PromotionDiscountItem("item", 100L, 10, 5);
        PromotionDiscountInfo promotionDiscountInfo = PromotionDiscountInfo.createCanGetMore(promotionDiscountItem, 5);
        PromotionDiscount promotionDiscount = new PromotionDiscount(List.of(promotionDiscountInfo));

        Purchase conflictedPurchase = purchase.applyPromotionDiscount(promotionDiscount);

        Assertions.assertThat(conflictedPurchase.getPromotionDiscount()).isEqualTo(promotionDiscount);
    }

    @Test
    @DisplayName("멤버십 할인 적용 시 할인 정보가 포함된 객체 생성 성공")
    void testApplyMembershipDiscount() {
        MembershipDiscount membershipDiscount = MembershipDiscount.createMembershipDiscount(5);
        purchase = purchase.applyPromotionDiscount(new PromotionDiscount(List.of()));
        purchase = purchase.resolveAllPromotionConflicts();
        Purchase discountedPurchase = purchase.applyMembershipDiscount(membershipDiscount);

        Assertions.assertThat(discountedPurchase.getMembershipDiscount()).isEqualTo(membershipDiscount);
    }

    @Test
    @DisplayName("존재하지 않는 프로모션 할인 정보에 접근할 때 예외 발생")
    void testGetPromotionDiscountThrowsException() {
        Assertions.assertThatThrownBy(purchase::getPromotionDiscount).isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("RESOLVED 상태가 아닌 경우 멤버십 할인 정보 접근 시 예외 발생")
    void testGetMembershipDiscountThrowsException() {
        Assertions.assertThatThrownBy(purchase::getMembershipDiscount).isInstanceOf(IllegalStateException.class);
    }
}