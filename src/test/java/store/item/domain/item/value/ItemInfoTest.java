package store.item.domain.item.value;

import java.time.LocalDateTime;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.common.util.IdHolder;
import store.item.controller.dto.request.ItemRequest;
import store.item.controller.dto.request.PromotionRuleRequest;
import store.item.domain.item.Item;
import store.item.domain.item.PromotionItem;
import store.item.domain.item.PromotionRule;

class ItemInfoTest {

    private Item item;
    private PromotionItem promotionItem;

    @BeforeEach
    void setUp() {
        IdHolder idHolder = new TestIdHolder();
        PromotionRule promotionRule = PromotionRule.create(
                new PromotionRuleRequest("promotion", 2, 1, LocalDateTime.now().minusDays(1),
                        LocalDateTime.now().plusDays(1)));
        ItemRequest itemRequest = new ItemRequest("name", 1000, 100, Optional.of("promotion"));

        item = Item.create(idHolder, itemRequest);
        promotionItem = PromotionItem.create(idHolder, itemRequest, promotionRule);
    }

    // 1. 생성 테스트
    @Test
    @DisplayName("item, promotionItem이 모두 존재하는 객체를 생성한다.")
    void testCreatePromotionItemInfo() {
        ItemInfo itemInfo = ItemInfo.createPromotionItemInfo(item, promotionItem);
        Assertions.assertThat(itemInfo.isPromotion()).isTrue();
    }

    @Test
    @DisplayName("item만  존재하는 객체를 생성한다.")
    void testCreateNotPromotionItemInfo() {
        ItemInfo itemInfo = ItemInfo.createNotPromotionItemInfo(item);
        Assertions.assertThat(itemInfo.isPromotion()).isFalse();
    }

    @Test
    @DisplayName("주문을 하면 수량만큼 차감한 후 새로운 객체를 반환한다.")
    void testPurchaseWithSufficientStock() {
        ItemInfo itemInfo = ItemInfo.createPromotionItemInfo(item, promotionItem);
        long purchaseAmount = 30;
        ItemInfo purchased = itemInfo.purchase(purchaseAmount);

        Assertions.assertThat(purchased.getPromotionItem().getStockQuantity())
                .isEqualTo(itemInfo.getPromotionItem().getStockQuantity() - purchaseAmount);
    }

    @Test
    @DisplayName("item, promotionItem을 초과하는 주문을 하면 예외를 반환한다.")
    void testPurchaseWithExceedingStock() {
        ItemInfo itemInfo = ItemInfo.createPromotionItemInfo(item, promotionItem);
        Assertions.assertThatThrownBy(() -> itemInfo.purchase(300L)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("구매량에 따라 프로모션을 적용해 반환한다.")
    void testJudgePromotionAmountWithinStock() {
        ItemInfo itemInfo = ItemInfo.createPromotionItemInfo(item, promotionItem);
        long buyAmount = 10L;
        long expectedAmount = 3L;

        long promotionAmount = itemInfo.judgePromotionAmount(buyAmount);

        Assertions.assertThat(promotionAmount).isEqualTo(expectedAmount);
        // 10을 구매 시 3개 프로모션 혜택 제공
    }

    @Test
    @DisplayName("프로모션 재고를 초과하는 주문을 하면, 프로모션 재고에 맞게 프로모션 수량을 제한한다.")
    void testJudgePromotionAmountExceedingStock() {
        ItemInfo itemInfo = ItemInfo.createPromotionItemInfo(item, promotionItem);
        long buyAmount = 160L;
        long expectedAmount = 33L;

        long promotionAmount = itemInfo.judgePromotionAmount(buyAmount);
        Assertions.assertThat(promotionAmount).isEqualTo(expectedAmount);
        // 프로모션 재고 100개에 맞게 프로모션 제한
    }

    @Test
    @DisplayName("프로모션 재고를 초과하는 주문을 하면, 프로모션 재고에 맞게 조정된 수량의 남는 구매량을 반환한다.")
    void testJudgeCannotPromotionAmount() {
        ItemInfo itemInfo = ItemInfo.createPromotionItemInfo(item, promotionItem);
        long buyAmount = 160L;
        long expectedAmount = 61L;

        long cannotPromotionAmount = itemInfo.judgeCannotPromotionAmount(buyAmount);
        Assertions.assertThat(cannotPromotionAmount).isEqualTo(expectedAmount);
        // 160개를 프로모션으로 구매하면, 프로모션 재고에 맞게 프로모션 적용하고 남는 구매량 반환
    }

    @Test
    @DisplayName("프로모션의 추가 프로모션 조건을 충족하면 true를 반환한다.")
    void testJudgeGetMorePromotion() {
        ItemInfo itemInfo = ItemInfo.createPromotionItemInfo(item, promotionItem);
        long buyAmount = 2L;

        Assertions.assertThat(itemInfo.judgeGetMorePromotion(buyAmount)).isTrue();
        // 2+1 추가 프로모션 조건 충족
    }

    @Test
    @DisplayName("promotion이 아닐 때 promotion에 접근하면 예외를 반환한다.")
    void testGetPromotionItemWhenNotPromotion() {
        ItemInfo itemInfo = ItemInfo.createNotPromotionItemInfo(item);
        Assertions.assertThatThrownBy(itemInfo::getPromotionItem).isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("item과 promotionItem의 합을 넘지 않는 수량의 주문을 하면 false 반환한다.")
    void testCanPurchaseWithSufficientStock() {
        ItemInfo itemInfo = ItemInfo.createPromotionItemInfo(item, promotionItem);
        Assertions.assertThat(itemInfo.canPurchase(70L)).isTrue();
    }

    @Test
    @DisplayName("item과 promotionItem의 합을 넘은 수량의 주문을 하면 false 반환한다.")
    void testCanPurchaseWithInsufficientStock() {
        ItemInfo itemInfo = ItemInfo.createPromotionItemInfo(item, promotionItem);
        Assertions.assertThat(itemInfo.canPurchase(300L)).isFalse();
    }

    @Test
    @DisplayName("ItemInfo에서 item 가격을 반환한다.")
    void testGetPrice() {
        ItemInfo itemInfo = ItemInfo.createNotPromotionItemInfo(item);
        Assertions.assertThat(itemInfo.getPrice()).isEqualTo(1000L);
    }

    // TEST용 IdHolder
    static class TestIdHolder implements IdHolder {
        @Override
        public String generateId() {
            return "test-id";
        }
    }
}