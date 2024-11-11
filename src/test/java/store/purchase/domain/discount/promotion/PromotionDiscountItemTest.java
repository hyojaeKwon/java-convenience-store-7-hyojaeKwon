package store.purchase.domain.discount.promotion;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PromotionDiscountItemTest {

    @Test
    @DisplayName("PromotionDiscountItem 객체가 정상적으로 생성된다")
    void testPromotionDiscountItemCreation() {
        PromotionDiscountItem discountItem = new PromotionDiscountItem("item1", 100L, 10, 2);

        Assertions.assertThat(discountItem.getName()).isEqualTo("item1");
        Assertions.assertThat(discountItem.getPrice()).isEqualTo(100L);
        Assertions.assertThat(discountItem.getBuyAmount()).isEqualTo(10);
        Assertions.assertThat(discountItem.getDiscountAmount()).isEqualTo(2);
    }

}