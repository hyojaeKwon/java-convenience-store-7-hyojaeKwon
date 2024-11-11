package store.purchase.domain.discount.promotion;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PromotionDiscountInfoTest {

    @Test
    @DisplayName("기본 생성된 PromotionDiscountInfo는 RESOLVED 상태이다")
    void testCreateResolvedDiscountInfo() {
        PromotionDiscountItem discountItem = new PromotionDiscountItem("item1", 100L, 10, 5);

        PromotionDiscountInfo discountInfo = PromotionDiscountInfo.create(discountItem);

        Assertions.assertThat(discountInfo.getStatus()).isEqualTo(PromotionDiscountStatus.RESOLVED);
        Assertions.assertThat(discountInfo.getStatusQuantity()).isZero();
        Assertions.assertThat(discountInfo.getPromotionDiscountItem()).isEqualTo(discountItem);
    }

    @Test
    @DisplayName("GET_MORE 상태의 PromotionDiscountInfo가 생성된다")
    void testCreateCanGetMoreDiscountInfo() {
        PromotionDiscountItem discountItem = new PromotionDiscountItem("item2", 200L, 5, 3);
        long quantity = 2;

        PromotionDiscountInfo discountInfo = PromotionDiscountInfo.createCanGetMore(discountItem, quantity);

        Assertions.assertThat(discountInfo.getStatus()).isEqualTo(PromotionDiscountStatus.GET_MORE);
        Assertions.assertThat(discountInfo.getStatusQuantity()).isEqualTo(quantity);
        Assertions.assertThat(discountInfo.getPromotionDiscountItem()).isEqualTo(discountItem);
    }

    @Test
    @DisplayName("CANT_DISCOUNT_ALL 상태의 PromotionDiscountInfo가 생성된다")
    void testCreateCannotDiscountAllDiscountInfo() {
        PromotionDiscountItem discountItem = new PromotionDiscountItem("item3", 150L, 8, 4);
        long quantity = 3;

        PromotionDiscountInfo discountInfo = PromotionDiscountInfo.createCannotDiscountAll(discountItem, quantity);

        Assertions.assertThat(discountInfo.getStatus()).isEqualTo(PromotionDiscountStatus.CANT_DISCOUNT_ALL);
        Assertions.assertThat(discountInfo.getStatusQuantity()).isEqualTo(quantity);
        Assertions.assertThat(discountInfo.getPromotionDiscountItem()).isEqualTo(discountItem);
    }

    @Test
    @DisplayName("GET_MORE 상태의 PromotionDiscountInfo를 RESOLVED 상태로 전환한다")
    void testResolveCanGetMore() {
        PromotionDiscountItem discountItem = new PromotionDiscountItem("item4", 100L, 10, 5);
        PromotionDiscountInfo discountInfo = PromotionDiscountInfo.createCanGetMore(discountItem, 1);

        PromotionDiscountInfo resolvedDiscountInfo = discountInfo.resolveCanGetMore();

        Assertions.assertThat(resolvedDiscountInfo.getStatus()).isEqualTo(PromotionDiscountStatus.RESOLVED);
        Assertions.assertThat(resolvedDiscountInfo.getPromotionDiscountItem().getBuyAmount()).isEqualTo(11);
        Assertions.assertThat(resolvedDiscountInfo.getPromotionDiscountItem().getDiscountAmount()).isEqualTo(6);
        Assertions.assertThat(resolvedDiscountInfo.getStatusQuantity()).isEqualTo(1);
    }
}