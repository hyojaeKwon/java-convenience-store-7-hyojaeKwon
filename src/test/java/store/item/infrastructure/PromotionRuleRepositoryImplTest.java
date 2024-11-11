package store.item.infrastructure;

import java.time.LocalDateTime;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.common.exception.repository.RepositoryException;
import store.item.controller.dto.request.PromotionRuleRequest;
import store.item.domain.item.PromotionRule;

class PromotionRuleRepositoryImplTest {

    private PromotionRuleRepositoryImpl promotionRuleRepository;
    private PromotionRule promotionRule1;

    @BeforeEach
    void setUp() {
        promotionRuleRepository = (PromotionRuleRepositoryImpl) PromotionRuleRepositoryImpl.getInstance();

        PromotionRuleRequest promotionRuleRequest1 = new PromotionRuleRequest("rule1", 1, 2,
                LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(1));
        PromotionRuleRequest promotionRuleRequest2 = new PromotionRuleRequest("rule2", 1, 2,
                LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(1));

        promotionRule1 = PromotionRule.create(promotionRuleRequest1);
    }

    @Test
    @DisplayName("ID가 null인 경우 저장 시 예외를 발생시킨다")
    void testSaveWithNullIdThrowsException() {
        Assertions.assertThatThrownBy(() -> promotionRuleRepository.save(null, promotionRule1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("프로모션 룰을 ID로 조회하면 해당 룰을 반환한다")
    void testFindById() {
        promotionRuleRepository.save(promotionRule1.getName(), promotionRule1);

        PromotionRule foundRule = promotionRuleRepository.findById(promotionRule1.getName());
        Assertions.assertThat(foundRule).isEqualTo(promotionRule1);
    }

    @Test
    @DisplayName("존재하지 않는 ID로 조회 시 예외를 발생시킨다")
    void testFindByIdNotFoundThrowsException() {
        Assertions.assertThatThrownBy(() -> promotionRuleRepository.findById("non-existing-id"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("프로모션 룰을 업데이트하면 업데이트된 룰을 반환한다")
    void testUpdate() {
        promotionRuleRepository.save(promotionRule1.getName(), promotionRule1);

        PromotionRuleRequest newPromotionRuleRequest = new PromotionRuleRequest("rule1",1,1,LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(1));
        PromotionRule updatedRule = PromotionRule.create(newPromotionRuleRequest);
        PromotionRule result = promotionRuleRepository.update(promotionRule1.getName(), updatedRule);

        Assertions.assertThat(result).isEqualTo(updatedRule);
        Assertions.assertThat(promotionRuleRepository.findById(updatedRule.getName())).isEqualTo(updatedRule);
    }

    @Test
    @DisplayName("프로모션 룰 이름으로 조회하면 해당 룰을 반환한다")
    void testFindByName() {
        promotionRuleRepository.save(promotionRule1.getName(), promotionRule1);

        Optional<PromotionRule> foundRule = promotionRuleRepository.findByName(promotionRule1.getName());

        Assertions.assertThat(foundRule).isPresent();
        Assertions.assertThat(foundRule).contains(promotionRule1);
    }

    @Test
    @DisplayName("존재하지 않는 이름으로 조회하면 빈 Optional을 반환한다")
    void testFindByNameNotFound() {
        Optional<PromotionRule> foundRule = promotionRuleRepository.findByName("Non-existing Rule");

        Assertions.assertThat(foundRule).isNotPresent();
    }

    @Test
    @DisplayName("프로모션 룰을 삭제하면 삭제된 룰을 반환한다")
    void testDelete() {
        promotionRuleRepository.save(promotionRule1.getName(), promotionRule1);

        PromotionRule deletedRule = promotionRuleRepository.delete(promotionRule1.getName());

        Assertions.assertThat(deletedRule).isEqualTo(promotionRule1);
        Assertions.assertThatThrownBy(() -> promotionRuleRepository.findById(promotionRule1.getName()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}