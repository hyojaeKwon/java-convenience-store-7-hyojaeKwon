package store.item.domain.item;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.common.util.IdHolder;
import store.common.util.SystemUuidHolder;
import store.item.controller.dto.request.ItemRequest;
import store.item.controller.dto.request.PromotionRuleRequest;

class PromotionItemTest {
    private IdHolder idHolder;
    private PromotionRule promotionRule;
    private ItemRequest validItemRequest;

    @BeforeEach
    void setUp() {
        idHolder = new SystemUuidHolder();
        promotionRule = PromotionRule.create(
                new PromotionRuleRequest("promotionRule", 2, 1, LocalDateTime.now().minusDays(1),
                        LocalDateTime.now().plusDays(1)));
        validItemRequest = new ItemRequest("Promotion", 1000, 10, Optional.of("promotionRule"));
    }

    @Test
    @DisplayName("성공적으로 PromotionItem 객체를 생성한다.")
    void createPromotionItemTest() {
        PromotionItem promotionItem = PromotionItem.create(idHolder, validItemRequest, promotionRule);

        assertNotNull(promotionItem);
        assertThat(promotionItem.getName()).isEqualTo(validItemRequest.getName());
        assertThat(promotionItem.getStockQuantity()).isEqualTo(validItemRequest.getQuantity());
    }

    @Test
    @DisplayName("PromotionRule이 존재하지 않는 ItemRequest로 생성 시 예외를 발생시킨다.")
    void createPromotionItemWithoutRuleTest() {
        ItemRequest itemRequestWithoutRule = new ItemRequest("Item", 1000, 10, Optional.empty());

        assertThatThrownBy(() -> PromotionItem.create(idHolder, itemRequestWithoutRule, promotionRule)).isInstanceOf(
                IllegalStateException.class);
    }

    @Test
    @DisplayName("stockQuantity가 충분할 때, 지정된 수량만큼 재고가 줄어든 PromotionItem 객체를 반환한다.")
    void purchaseTest() {
        PromotionItem promotionItem = PromotionItem.create(idHolder, validItemRequest, promotionRule);
        PromotionItem updatedPromotionItem = promotionItem.purchase(5);

        assertThat(updatedPromotionItem.getStockQuantity()).isEqualTo(5);
    }

    @Test
    @DisplayName("구매하려는 수량이 stockQuantity보다 큰 경우 예외를 발생시킨다.")
    void purchaseExceedQuantityTest() {
        PromotionItem promotionItem = PromotionItem.create(idHolder, validItemRequest, promotionRule);

        assertThatThrownBy(() -> promotionItem.purchase(20)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("getPromotionItemId, getName, getStockQuantity 메서드가 올바른 값을 반환한다.")
    void gettersTest() {
        PromotionItem promotionItem = PromotionItem.create(idHolder, validItemRequest, promotionRule);

        assertThat(promotionItem.getPromotionItemId()).isNotNull();
        assertThat(promotionItem.getName()).isEqualTo(validItemRequest.getName());
        assertThat(promotionItem.getStockQuantity()).isEqualTo(validItemRequest.getQuantity());
    }

    @Test
    @DisplayName("PromotionRule에서 제공하는 총 프로모션 수량을 올바르게 반환한다.")
    void getPromotionRuleQuantitySumTest() {
        PromotionItem promotionItem = PromotionItem.create(idHolder, validItemRequest, promotionRule);

        assertThat(promotionItem.getPromotionRuleQuantitySum()).isEqualTo(promotionRule.getPromotionQuantitySum());
    }

    @Test
    @DisplayName("특정 구매 수량에 따른 올바른 프로모션 수량을 반환한다.")
    void getPromotionQuantityTest() {
        PromotionItem promotionItem = PromotionItem.create(idHolder, validItemRequest, promotionRule);

        long buyQuantity = 10;
        long expectedPromotionQuantity = promotionRule.getPromotionQuantity(buyQuantity);
        assertThat(promotionItem.getPromotionQuantity(buyQuantity)).isEqualTo(expectedPromotionQuantity);
    }

    @Test
    @DisplayName("추가 프로모션 가능 여부를 올바르게 판단한다.")
    void judgeCanGetOneMoreTest() {
        PromotionItem promotionItem = PromotionItem.create(idHolder, validItemRequest, promotionRule);

        long buyQuantity = 5;
        assertThat(promotionItem.judgeCanGetOneMore(buyQuantity)).isEqualTo(promotionRule.canGetOneMore(buyQuantity));
    }

    @Test
    @DisplayName("getPromotionRule 메서드가 PromotionRule 객체를 올바르게 반환한다.")
    void getPromotionRuleTest() {
        PromotionItem promotionItem = PromotionItem.create(idHolder, validItemRequest, promotionRule);

        assertThat(promotionItem.getPromotionRule()).isSameAs(promotionRule);
    }

    @Test
    @DisplayName("날짜에 따라서 프로모션 활성 상태를 올바르게 판단한다.")
    void isActiveTest() {
        PromotionItem promotionItem = PromotionItem.create(idHolder, validItemRequest, promotionRule);

        assertThat(promotionItem.isActive(LocalDateTime.now())).isTrue();

        LocalDateTime pastDate = LocalDateTime.now().minusDays(10);
        assertThat(promotionItem.isActive(pastDate)).isFalse();
    }

}