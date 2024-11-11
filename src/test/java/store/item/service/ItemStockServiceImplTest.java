package store.item.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.common.util.IdHolder;
import store.common.util.SystemUuidHolder;
import store.item.controller.ItemStockService;
import store.item.controller.dto.request.ItemRequest;
import store.item.controller.dto.request.PromotionRuleRequest;
import store.item.domain.item.Item;
import store.item.domain.item.PromotionItem;
import store.item.domain.item.PromotionRule;
import store.item.domain.item.value.ItemInfo;
import store.item.infrastructure.mock.MockItemRepository;
import store.item.infrastructure.mock.MockPromotionRepository;
import store.item.service.repository.ItemRepository;
import store.item.service.repository.PromotionItemRepository;

class ItemStockServiceImplTest {

    private ItemRepository itemRepository;
    private PromotionItemRepository promotionItemRepository;
    private ItemStockService itemStockService;
    private ItemRequest itemRequest;
    private PromotionRuleRequest ruleRequest;
    private IdHolder idHolder;

    @BeforeEach
    void setUp() {
        idHolder = new SystemUuidHolder();
        itemRequest = new ItemRequest("item", 100, 10, Optional.of("rule"));
        ruleRequest = new PromotionRuleRequest("rule", 1, 2, LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(1));

        itemRepository = new MockItemRepository();
        promotionItemRepository = new MockPromotionRepository();
        itemStockService = new ItemStockServiceImpl(itemRepository, promotionItemRepository);
    }

    @Test
    @DisplayName("일반 아이템 재고를 업데이트 한다.")
    void testUpdateItemStockWithGeneralItem() {
        Item item = Item.create(idHolder, itemRequest);
        itemRepository.save(item.getId(), item);

        item.purchase(1);
        ItemInfo itemInfo = ItemInfo.createNotPromotionItemInfo(item);

        itemStockService.updateItemStock(itemInfo);

        Assertions.assertThat(itemRepository.findByName(item.getName())).isPresent();
        Assertions.assertThat(itemRepository.findByName(item.getName()).get().getStockQuantity())
                .isEqualTo(item.getStockQuantity());
    }

    @Test
    @DisplayName("프로모션 아이템 재고를 업데이트 한다.")
    void testUpdateItemStockWithPromotionItem() {
        Item item = Item.create(idHolder, itemRequest);
        PromotionItem promotionItem = PromotionItem.create(idHolder, itemRequest, PromotionRule.create(ruleRequest));

        itemRepository.save(item.getId(), item);
        promotionItemRepository.save(promotionItem.getPromotionItemId(), promotionItem);

        promotionItem.purchase(1);
        ItemInfo itemInfo = ItemInfo.createPromotionItemInfo(item, promotionItem);

        itemStockService.updateItemStock(itemInfo);

        Assertions.assertThat(promotionItemRepository.findByName(promotionItem.getName())).isPresent();
        Assertions.assertThat(promotionItemRepository.findByName(promotionItem.getName()).get().getStockQuantity())
                .isEqualTo(promotionItem.getStockQuantity());
    }

    @Test
    @DisplayName("일반 및 프로모션 아이템 동시 재고 업데이트")
    void testUpdateItemStockWithGeneralAndPromotionItems() {
        Item item = Item.create(idHolder, itemRequest);
        PromotionItem promotionItem = PromotionItem.create(idHolder, itemRequest, PromotionRule.create(ruleRequest));

        itemRepository.save(item.getId(), item);
        promotionItemRepository.save(promotionItem.getPromotionItemId(), promotionItem);

        item.purchase(1);
        promotionItem.purchase(1);
        ItemInfo itemInfo = ItemInfo.createPromotionItemInfo(item, promotionItem);

        itemStockService.updateItemStock(itemInfo);

        Assertions.assertThat(itemRepository.findByName(item.getName())).isPresent();
        Assertions.assertThat(itemRepository.findByName(item.getName()).get().getStockQuantity())
                .isEqualTo(item.getStockQuantity());
        Assertions.assertThat(promotionItemRepository.findByName(promotionItem.getName())).isPresent();
        Assertions.assertThat(promotionItemRepository.findByName(promotionItem.getName()).get().getStockQuantity())
                .isEqualTo(promotionItem.getStockQuantity());
    }

    @Test
    @DisplayName("일반 및 프로모션 아이템 포함 목록을 조회한다.")
    void testGetAllItemInfoWithGeneralAndPromotionItems() {
        PromotionItem promotionItem = PromotionItem.create(idHolder, itemRequest, PromotionRule.create(ruleRequest));
        Item item = Item.create(idHolder, new ItemRequest(itemRequest.getName(), 100, 10, Optional.of(
                ruleRequest.getName())));

        itemRepository.save(item.getId(), item);
        promotionItemRepository.save(promotionItem.getPromotionItemId(), promotionItem);

        List<ItemInfo> itemInfos = itemStockService.getAllItemInfo();

        Assertions.assertThat(itemInfos).hasSize(1);
        Assertions.assertThat(itemInfos).anyMatch(info -> info.getItem().equals(item) && info.isPromotion());
        Assertions.assertThat(itemInfos)
                .anyMatch(info -> info.getPromotionItem().equals(promotionItem) && info.isPromotion());
    }

    @Test
    @DisplayName("일반 아이템 이름으로 재고 정보를 조회한다.")
    void testGetItemInfoByNameWithGeneralItem() {
        Item item = Item.create(idHolder, new ItemRequest("Item1", 100, 10, Optional.empty()));
        itemRepository.save(item.getId(), item);

        ItemInfo itemInfo = itemStockService.getItemInfoByName("Item1");

        Assertions.assertThat(itemInfo.getItem()).isEqualTo(item);
        Assertions.assertThat(itemInfo.isPromotion()).isFalse();
    }

    @Test
    @DisplayName("프로모션 아이템 이름으로 재고 정보를 조회한다.")
    void testGetItemInfoByNameWithPromotionItem() {
        Item item = Item.create(idHolder, itemRequest);
        PromotionItem promotionItem = PromotionItem.create(idHolder, itemRequest, PromotionRule.create(ruleRequest));

        itemRepository.save(item.getId(), item);
        promotionItemRepository.save(promotionItem.getPromotionItemId(), promotionItem);

        ItemInfo itemInfo = itemStockService.getItemInfoByName("item");

        Assertions.assertThat(itemInfo.getItem()).isEqualTo(item);
        Assertions.assertThat(itemInfo.getPromotionItem()).isEqualTo(promotionItem);
        Assertions.assertThat(itemInfo.isPromotion()).isTrue();
    }

    @Test
    @DisplayName("존재하지 않는 아이템 이름으로 재고 정보 조회 시 예외 발생")
    void testGetItemInfoByNameNotFound() {
        assertThatThrownBy(() -> itemStockService.getItemInfoByName("NonExistingItem")).isInstanceOf(
                IllegalArgumentException.class);
    }
}