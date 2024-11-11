package store.item.config;

import store.common.util.IdHolder;
import store.common.util.SystemUuidHolder;
import store.item.controller.ItemController;
import store.item.controller.ItemPurchaseService;
import store.item.controller.ItemSaveService;
import store.item.controller.ItemStockService;
import store.item.infrastructure.ItemRepositoryImpl;
import store.item.infrastructure.PromotionRepositoryImpl;
import store.item.infrastructure.PromotionRuleRepositoryImpl;
import store.item.service.ItemPurchaseServiceImpl;
import store.item.service.ItemSaveServiceImpl;
import store.item.service.ItemStockServiceImpl;
import store.item.service.repository.ItemRepository;
import store.item.service.repository.PromotionItemRepository;
import store.item.service.repository.PromotionRuleRepository;

public class ItemConfig {

    public ItemController itemController() {
        return new ItemController(itemSaveService(), itemStockService());
    }

    public ItemPurchaseService itemPurchaseService() {
        return new ItemPurchaseServiceImpl(itemRepository(), promotionItemRepository(), itemStockService());
    }

    public ItemStockService itemStockService() {
        return new ItemStockServiceImpl(itemRepository(), promotionItemRepository());
    }

    public ItemSaveService itemSaveService() {
        return new ItemSaveServiceImpl(itemRepository(), promotionItemRepository(), promotionRuleRepository(),
                idHolder());
    }

    private IdHolder idHolder() {
        return new SystemUuidHolder();
    }

    private PromotionRuleRepository promotionRuleRepository() {
        return PromotionRuleRepositoryImpl.getInstance();
    }

    private PromotionItemRepository promotionItemRepository() {
        return PromotionRepositoryImpl.getInstance();
    }

    private ItemRepository itemRepository() {
        return ItemRepositoryImpl.getInstance();
    }
}
