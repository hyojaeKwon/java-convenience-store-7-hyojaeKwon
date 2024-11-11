package store.item.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.common.util.IdHolder;
import store.common.util.SystemUuidHolder;
import store.item.controller.dto.request.ItemRequest;
import store.item.controller.dto.request.ItemSaveRequest;
import store.item.controller.dto.request.PromotionRuleRequest;
import store.item.domain.item.PromotionRule;
import store.item.infrastructure.mock.MockItemRepository;
import store.item.infrastructure.mock.MockPromotionRepository;
import store.item.infrastructure.mock.MockRuleRepository;
import store.item.service.repository.ItemRepository;
import store.item.service.repository.PromotionItemRepository;
import store.item.service.repository.PromotionRuleRepository;

class ItemSaveServiceImplTest {

    private ItemRepository itemRepository;
    private PromotionItemRepository promotionItemRepository;
    private PromotionRuleRepository promotionRuleRepository;
    private IdHolder idHolder;
    private ItemSaveServiceImpl itemSaveService;

    @BeforeEach
    void setUp() {
        itemRepository = new MockItemRepository();
        promotionItemRepository = new MockPromotionRepository();
        promotionRuleRepository = new MockRuleRepository();
        idHolder = new SystemUuidHolder();

        itemSaveService = new ItemSaveServiceImpl(itemRepository, promotionItemRepository, promotionRuleRepository,
                idHolder);
    }

    @Test
    @DisplayName("프로모션 룰을 저장한다.")
    void testSaveRule() {
        PromotionRuleRequest ruleRequest1 = new PromotionRuleRequest("rule1", 10, 5, LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(1));
        PromotionRuleRequest ruleRequest2 = new PromotionRuleRequest("rule2", 20, 10, LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(1));
        List<PromotionRuleRequest> ruleRequests = List.of(ruleRequest1, ruleRequest2);

        itemSaveService.saveRule(ruleRequests);

        assertThat(promotionRuleRepository.findByName("rule1")).isPresent();
        assertThat(promotionRuleRepository.findByName("rule2")).isPresent();
    }

    @Test
    @DisplayName("아이템 저장 요청 시 프로모션 아이템과 일반 아이템을 각각 저장한다")
    void testSave() {
        ItemRequest itemRequest1 = new ItemRequest("item", 100, 10, Optional.empty());
        ItemRequest itemRequest2 = new ItemRequest("promoItem", 200, 20, Optional.of("rule"));
        List<ItemRequest> itemRequests = List.of(itemRequest1, itemRequest2);

        PromotionRuleRequest ruleRequest = new PromotionRuleRequest("rule", 10, 5, LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(1));
        PromotionRule promotionRule = PromotionRule.create(ruleRequest);
        promotionRuleRepository.save(promotionRule.getName(), promotionRule);

        ItemSaveRequest itemSaveRequest = new ItemSaveRequest(itemRequests);

        itemSaveService.save(itemSaveRequest);

        assertThat(itemRepository.findByName("item")).isPresent();
        assertThat(promotionItemRepository.findByName("promoItem")).isPresent();
    }

    @Test
    @DisplayName("존재하지 않는 프로모션 룰 이름으로 아이템 저장 요청 시 예외를 발생시킨다")
    void testSaveWithNonExistingPromotionRuleThrowsException() {
        ItemRequest itemRequest = new ItemRequest("promoItem", 200, 20, Optional.of("nonExistingRule"));
        List<ItemRequest> itemRequests = List.of(itemRequest);
        ItemSaveRequest itemSaveRequest = new ItemSaveRequest(itemRequests);

        assertThatThrownBy(() -> itemSaveService.save(itemSaveRequest)).isInstanceOf(IllegalArgumentException.class);
    }
}