package store.item.infrastructure;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.common.util.IdHolder;
import store.item.controller.dto.request.ItemRequest;
import store.item.controller.dto.request.PromotionRuleRequest;
import store.item.domain.item.PromotionItem;
import store.item.domain.item.PromotionRule;

class PromotionRepositoryImplTest {

    private PromotionRepositoryImpl promotionRepository;

    private PromotionItem promotionItem1;
    private PromotionItem promotionItem2;
    private PromotionItem nullPromotionItem;

    @BeforeEach
    void setUp() {
        IdHolder nullHolder = new NullIdHolder();
        IdHolder testHolder = new TestHolder();

        ItemRequest itemRequest1 = new ItemRequest("item1", 100, 100, Optional.of("test"));
        ItemRequest itemRequest2 = new ItemRequest("item2", 100, 100, Optional.of("test"));

        PromotionRuleRequest promotionRuleRequest = new PromotionRuleRequest("test", 2, 1,
                LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(1));
        PromotionRule promotionRule = PromotionRule.create(promotionRuleRequest);

        promotionItem1 = PromotionItem.create(testHolder, itemRequest1, promotionRule);
        promotionItem2 = PromotionItem.create(testHolder, itemRequest2, promotionRule);
        nullPromotionItem = PromotionItem.create(nullHolder, itemRequest1, promotionRule);

        promotionRepository = (PromotionRepositoryImpl) PromotionRepositoryImpl.getInstance();
        promotionRepository.findAll().forEach(item -> promotionRepository.delete(item.getPromotionItemId()));
    }

    @Test
    @DisplayName("새로운 프로모션 아이템을 저장하면 해당 아이템을 반환한다")
    void testSave() {
        PromotionItem savedItem = promotionRepository.save(promotionItem1.getPromotionItemId(), promotionItem1);

        Assertions.assertThat(savedItem).isEqualTo(promotionItem1);
        Assertions.assertThat(promotionRepository.findById(promotionItem1.getPromotionItemId()))
                .isEqualTo(promotionItem1);
    }

    @Test
    @DisplayName("ID가 null인 경우 저장 시 예외를 발생시킨다")
    void testSaveWithNullIdThrowsException() {
        Assertions.assertThatThrownBy(
                        () -> promotionRepository.save(nullPromotionItem.getPromotionItemId(), nullPromotionItem))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("프로모션 아이템을 ID로 조회하면 해당 아이템을 반환한다")
    void testFindById() {
        promotionRepository.save(promotionItem1.getPromotionItemId(), promotionItem1);

        PromotionItem foundItem = promotionRepository.findById(promotionItem1.getPromotionItemId());
        Assertions.assertThat(foundItem).isEqualTo(promotionItem1);
    }

    @Test
    @DisplayName("존재하지 않는 ID로 조회 시 예외를 발생시킨다")
    void testFindByIdNotFoundThrowsException() {
        Assertions.assertThatThrownBy(() -> promotionRepository.findById("non-existing-id"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("프로모션 아이템을 업데이트하면 업데이트된 아이템을 반환한다")
    void testUpdate() {
        promotionRepository.save(promotionItem1.getPromotionItemId(), promotionItem1);

        PromotionItem updatedPromotionItem = promotionItem1.purchase(50);
        PromotionItem result = promotionRepository.update(promotionItem1.getPromotionItemId(), updatedPromotionItem);

        Assertions.assertThat(result).isEqualTo(updatedPromotionItem);
        Assertions.assertThat(promotionRepository.findById(updatedPromotionItem.getPromotionItemId()))
                .isEqualTo(updatedPromotionItem);
    }

    @Test
    @DisplayName("프로모션 아이템 이름으로 조회하면 해당 아이템을 반환한다")
    void testFindByName() {
        promotionRepository.save(promotionItem1.getPromotionItemId(), promotionItem1);

        Optional<PromotionItem> foundItem = promotionRepository.findByName(promotionItem1.getName());

        Assertions.assertThat(foundItem).isPresent();
        Assertions.assertThat(foundItem).contains(promotionItem1);
    }

    @Test
    @DisplayName("존재하지 않는 이름으로 조회하면 빈 Optional을 반환한다")
    void testFindByNameNotFound() {
        Optional<PromotionItem> foundItem = promotionRepository.findByName("Non-existing PromoItem");

        Assertions.assertThat(foundItem).isNotPresent();
    }

    @Test
    @DisplayName("프로모션 아이템을 삭제하면 삭제된 아이템을 반환한다")
    void testDelete() {
        promotionRepository.save(promotionItem1.getPromotionItemId(), promotionItem1);

        PromotionItem deletedItem = promotionRepository.delete(promotionItem1.getPromotionItemId());

        Assertions.assertThat(deletedItem).isEqualTo(promotionItem1);
        Assertions.assertThatThrownBy(() -> promotionRepository.findById(promotionItem1.getPromotionItemId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("저장된 모든 프로모션 아이템을 반환한다")
    void testFindAll() {
        promotionRepository.save(promotionItem1.getPromotionItemId(), promotionItem1);
        promotionRepository.save(promotionItem2.getPromotionItemId(), promotionItem2);

        List<PromotionItem> items = promotionRepository.findAll();

        Assertions.assertThat(items.getFirst().getPromotionItemId()).isEqualTo(promotionItem1.getPromotionItemId());
        Assertions.assertThat(items.getLast().getPromotionItemId()).isEqualTo(promotionItem2.getPromotionItemId());
    }

    static class NullIdHolder implements IdHolder {
        @Override
        public String generateId() {
            return null;
        }
    }

    static class TestHolder implements IdHolder {
        @Override
        public String generateId() {
            return "test";
        }
    }
}