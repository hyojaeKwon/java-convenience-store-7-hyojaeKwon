package store.item.controller;

import java.util.List;
import store.item.controller.dto.request.ItemSaveRequest;
import store.item.controller.dto.request.PromotionRuleRequest;

public class ItemController {

    private final ItemSaveService itemSaveService;

    public ItemController(ItemSaveService itemSaveService) {
        this.itemSaveService = itemSaveService;
    }

    public void saveItem(ItemSaveRequest itemSaveRequest) {
        itemSaveService.save(itemSaveRequest);
    }

    public void savePromotion(List<PromotionRuleRequest> request) {
        itemSaveService.saveRule(request);
    }

    public void getAllItemInfo() {
        // TODO
    }
}
