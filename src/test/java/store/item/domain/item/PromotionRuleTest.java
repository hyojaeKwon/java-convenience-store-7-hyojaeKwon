package store.item.domain.item;

import java.time.LocalDateTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.item.controller.dto.request.PromotionRuleRequest;

class PromotionRuleTest {

    private PromotionRuleRequest promotionRuleRequest;

    @BeforeEach
    void setUp() {
        promotionRuleRequest = new PromotionRuleRequest("Promotion", 2, 1, LocalDateTime.now().minusDays(2),
                LocalDateTime.now().plusDays(2));
    }

    @Test
    @DisplayName("정상적인 PromotionRuleReuqest객체로 PromotionRule을 생성한다.")
    void createPromotionRuleTest() {
        PromotionRule promotionRule = PromotionRule.create(promotionRuleRequest);

        Assertions.assertThat(promotionRule.getName()).isEqualTo(promotionRule.getName());
    }

    @Test
    @DisplayName("이름이 null이거나 빈 문자열일 경우 예외를 발생시킨다.")
    void createInvalidNameRuleTest() {
        PromotionRuleRequest invalidRequest = new PromotionRuleRequest("", 1, 1, LocalDateTime.now().minusDays(2),
                LocalDateTime.now().plusDays(2));

        Assertions.assertThatThrownBy(() -> PromotionRule.create(invalidRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("buyQuantity 또는 promotionQuantity가 0 이하일 경우 예외를 발생시킨다.")
    void createInvalidQuantityRuleTest() {
        PromotionRuleRequest invalidRequest = new PromotionRuleRequest("invalidRequest", 0, 0,
                LocalDateTime.now().minusDays(2), LocalDateTime.now().plusDays(2));

        Assertions.assertThatThrownBy(() -> PromotionRule.create(invalidRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("endDate가 startDate와 같거나 이전일 경우 예외를 발생시킨다.")
    void createPromotionRuleInvalidDateRangeTest() {
        PromotionRuleRequest invalidRequest = new PromotionRuleRequest("invalidRequest", 5, 2, LocalDateTime.now(),
                LocalDateTime.now().minusDays(1));

        Assertions.assertThatThrownBy(() -> PromotionRule.create(invalidRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("현재 날짜가 프로모션 기간 사이일 때 true 반환한다.")
    void isPromotionRuleValidTest() {
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);
        PromotionRuleRequest request = new PromotionRuleRequest("promotionRequest", 5, 2, startDate, endDate);

        PromotionRule promotionRule = PromotionRule.create(request);

        Assertions.assertThat(promotionRule.isPromotionDate(LocalDateTime.now())).isTrue();
    }

    @Test
    @DisplayName("현재 날짜가 프로모션 startDate 이전일 때 false 반환한다.")
    void isPromotionNotStartTest() {
        LocalDateTime startDate = LocalDateTime.now().plusDays(1);
        LocalDateTime endDate = LocalDateTime.now().plusDays(2);
        PromotionRuleRequest request = new PromotionRuleRequest("promotionRequest", 5, 2, startDate, endDate);

        PromotionRule promotionRule = PromotionRule.create(request);

        Assertions.assertThat(promotionRule.isPromotionDate(LocalDateTime.now())).isFalse();
    }

    @Test
    @DisplayName("현재 날짜가 프로모션 endDate 이후일 때 false 반환한다.")
    void isPromotionAlreadyEndTest() {
        LocalDateTime startDate = LocalDateTime.now().minusDays(2);
        LocalDateTime endDate = LocalDateTime.now().minusDays(1);
        PromotionRuleRequest request = new PromotionRuleRequest("promotionRequest", 5, 2, startDate, endDate);

        PromotionRule promotionRule = PromotionRule.create(request);

        Assertions.assertThat(promotionRule.isPromotionDate(LocalDateTime.now())).isFalse();
    }

    // - 특정 buyQuantityInput에 따라 올바른 프로모션 수량이 반환되는지 테스트
    @Test
    @DisplayName("buyQuantityInput에 따라 올바른 프로모션 수량을 반환한다.")
    void buyQuantityInputTest() {
        PromotionRule promotionRule = PromotionRule.create(promotionRuleRequest);
        int buyQuantity4 = 4;
        int expectedQuantity4 = 1;
        Assertions.assertThat(promotionRule.getPromotionQuantity(buyQuantity4)).isEqualTo(expectedQuantity4);

        int buyQuantity5 = 5;
        int expectedQuantity5 = 1;
        Assertions.assertThat(promotionRule.getPromotionQuantity(buyQuantity5)).isEqualTo(expectedQuantity5);

        int buyQuantity6 = 6;
        int expectedQuantity6 = 2;
        Assertions.assertThat(promotionRule.getPromotionQuantity(buyQuantity6)).isEqualTo(expectedQuantity6);
    }

    @Test
    @DisplayName("buyQuantityInput이 프로모션 조건을 충족하지 못하면 0을 반환한다.")
    void buyQuantityInputInvalidQuantityTest() {
        PromotionRule promotionRule = PromotionRule.create(promotionRuleRequest);

        int buyQuantity = 1;
        int expectedQuantity = 0;
        Assertions.assertThat(promotionRule.getPromotionQuantity(buyQuantity)).isEqualTo(expectedQuantity);
    }

    @Test
    @DisplayName("추가 프로모션 조건을 충족하는 경우 true를 반환한다.")
    void canGetOneMoreTest() {
        PromotionRule promotionRule = PromotionRule.create(promotionRuleRequest);

        int buyQuantity = 2;

        Assertions.assertThat(promotionRule.canGetOneMore(buyQuantity)).isTrue();
    }

    @Test
    @DisplayName("추가 프로모션 조건을 충족하지 않는 경우 false를 반환한다.")
    void cannotGetOneMoreTest() {
        PromotionRule promotionRule = PromotionRule.create(promotionRuleRequest);

        int buyQuantity = 3;

        Assertions.assertThat(promotionRule.canGetOneMore(buyQuantity)).isFalse();
    }
}
