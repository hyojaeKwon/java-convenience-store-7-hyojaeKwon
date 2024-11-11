package store.purchase.domain.discount.promotion;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PromotionDiscountTest {

    @Test
    @DisplayName("PromotionDiscount 객체가 정상적으로 생성된다")
    void testPromotionDiscountCreation() {
        PromotionDiscountItem item1 = new PromotionDiscountItem("item1", 100L, 10, 2);
        PromotionDiscountInfo info1 = PromotionDiscountInfo.create(item1);

        PromotionDiscount promotionDiscount = new PromotionDiscount(List.of(info1));

        Assertions.assertThat(promotionDiscount.getPromotionDiscountsInfos()).containsExactly(info1);
    }

    @Test
    @DisplayName("이름에 해당하는 프로모션 할인 정보의 CONFLICT 상태를 RESOLVED 상태로 전환한다")
    void testResolveGetMoreConflict() {
        PromotionDiscountItem item1 = new PromotionDiscountItem("item1", 100L, 10, 2);
        PromotionDiscountItem item2 = new PromotionDiscountItem("item2", 200L, 5, 3);

        PromotionDiscountInfo info1 = PromotionDiscountInfo.createCanGetMore(item1, 1);
        PromotionDiscountInfo info2 = PromotionDiscountInfo.create(item2);

        PromotionDiscount promotionDiscount = new PromotionDiscount(List.of(info1, info2));
        PromotionDiscount resolvedDiscount = promotionDiscount.resolveGetMoreConflict("item1");

        Assertions.assertThat(resolvedDiscount.getPromotionDiscountsInfos().get(0).getStatus())
                .isEqualTo(PromotionDiscountStatus.RESOLVED);
        Assertions.assertThat(
                        resolvedDiscount.getPromotionDiscountsInfos().get(0).getPromotionDiscountItem().getBuyAmount())
                .isEqualTo(11);
    }

    @Test
    @DisplayName("할인 금액의 총합을 올바르게 계산한다")
    void testSumOfDiscountAmount() {
        PromotionDiscountItem item1 = new PromotionDiscountItem("item1", 100L, 10, 2); // 할인액 = 2 * 100 = 200
        PromotionDiscountItem item2 = new PromotionDiscountItem("item2", 200L, 5, 3);  // 할인액 = 3 * 200 = 600

        PromotionDiscountInfo info1 = PromotionDiscountInfo.create(item1);
        PromotionDiscountInfo info2 = PromotionDiscountInfo.create(item2);

        PromotionDiscount promotionDiscount = new PromotionDiscount(List.of(info1, info2));

        long totalDiscountAmount = promotionDiscount.sumOfDiscountAmount();

        Assertions.assertThat(totalDiscountAmount).isEqualTo(800L);
    }
}