package store.item.controller;

import java.util.List;
import store.item.controller.dto.request.ItemSaveRequest;
import store.item.controller.dto.request.PromotionRuleRequest;
import store.item.domain.item.value.ItemInfo;

public class ItemController {

    private final ItemSaveService itemSaveService;
    private final ItemStockService itemStockService;

    public ItemController(ItemSaveService itemSaveService, ItemStockService itemStockService) {
        this.itemSaveService = itemSaveService;
        this.itemStockService = itemStockService;
    }

    public void saveItem(ItemSaveRequest itemSaveRequest) {
        itemSaveService.save(itemSaveRequest);
    }

    public void savePromotion(List<PromotionRuleRequest> request) {
        itemSaveService.saveRule(request);
    }

    public List<ItemInfo> getAllItemInfo() {
        return itemStockService.getAllItemInfo();
    }
}
