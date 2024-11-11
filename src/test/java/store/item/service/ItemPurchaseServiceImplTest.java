package store.item.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.common.util.IdHolder;
import store.common.util.SystemUuidHolder;
import store.item.controller.dto.request.ItemRequest;
import store.item.controller.dto.request.PromotionRuleRequest;
import store.item.domain.item.Item;
import store.item.domain.item.PromotionItem;
import store.item.domain.item.PromotionRule;
import store.item.infrastructure.mock.MockItemRepository;
import store.item.infrastructure.mock.MockPromotionRepository;
import store.item.service.mock.MockItemStockService;
import store.item.service.repository.ItemRepository;
import store.item.service.repository.PromotionItemRepository;

class ItemPurchaseServiceImplTest {

    private ItemRepository itemRepository;
    private PromotionItemRepository promotionItemRepository;
    private ItemPurchaseServiceImpl itemPurchaseService;
    private IdHolder idHolder;
    private ItemRequest itemRequest;
    private PromotionRuleRequest ruleRequest;

    @BeforeEach
    void setUp() {
        idHolder = new SystemUuidHolder();
        itemRequest = new ItemRequest("item", 100, 10, Optional.of("rule"));
        ruleRequest = new PromotionRuleRequest("rule", 1, 1, LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(1));

        itemRepository = new MockItemRepository();
        promotionItemRepository = new MockPromotionRepository();
        itemPurchaseService = new ItemPurchaseServiceImpl(itemRepository, promotionItemRepository,
                new MockItemStockService());
    }

    @Test
    @DisplayName("프로모션 혜택이 부족할 경우 가능한 범위 내에서 혜택이 적용된다.")
    void testPurchaseWithInsufficientPromotionStock() {
        Item item = Item.create(idHolder, itemRequest);
        PromotionRule promotionRule = PromotionRule.create(ruleRequest);
        PromotionItem promotionItem = PromotionItem.create(idHolder, itemRequest, promotionRule);

        itemRepository.save(item.getId(), item);
        promotionItemRepository.save(promotionItem.getPromotionItemId(), promotionItem);

        itemPurchaseService.purchase("item", 10);

        PromotionItem updatedPromotionItem = promotionItemRepository.findByName("item").get();
        assertThat(updatedPromotionItem.getStockQuantity()).isEqualTo(10);
    }

    @Test
    @DisplayName("존재하지 않는 아이템 구매 시 예외 발생")
    void testPurchaseWithNonExistingItem() {
        assertThatThrownBy(() -> itemPurchaseService.purchase("NonExistingItem", 10)).isInstanceOf(
                IllegalArgumentException.class);
    }

    @Test
    @DisplayName("프로모션 규칙이 있는 아이템과 없는 아이템을 모두 구매할 수 있다.")
    void testPurchaseWithMixedItems() {
        Item item = Item.create(idHolder, new ItemRequest("item", 50, 5, Optional.empty()));
        itemRepository.save(item.getId(), item);

        Item promoItem = Item.create(idHolder, itemRequest);
        PromotionRule promotionRule = PromotionRule.create(ruleRequest);
        PromotionItem promotionItem = PromotionItem.create(idHolder, itemRequest, promotionRule);
        promotionItemRepository.save(promotionItem.getPromotionItemId(), promotionItem);

        // 아이템 구매
        itemPurchaseService.purchase("item", 5);
        Item updatedGeneralItem = itemRepository.findByName("item").get();
        assertThat(updatedGeneralItem.getStockQuantity()).isEqualTo(5);
    }
}