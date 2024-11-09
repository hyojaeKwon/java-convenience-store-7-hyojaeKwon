package store.user.controller.input;

import java.util.List;
import store.user.controller.input.converter.ResourceItemConverter;
import store.user.controller.input.converter.ResourcePromotionConverter;
import store.item.controller.ItemController;
import store.item.controller.dto.request.ItemSaveRequest;
import store.item.controller.dto.request.PromotionRuleRequest;

public class InputController {

    private final ItemController itemController;
    private final ResourcePromotionConverter resourcePromotionConverter;
    private final ResourceItemConverter resourceItemConverter;

    public InputController(ItemController itemController, ResourcePromotionConverter resourcePromotionConverter,
                           ResourceItemConverter resourceItemConverter) {
        this.itemController = itemController;
        this.resourcePromotionConverter = resourcePromotionConverter;
        this.resourceItemConverter = resourceItemConverter;
    }

    public void saveResources() {
        saveRules();
        saveItems();
    }

    private void saveRules() {
        List<PromotionRuleRequest> ruleRequest = resourcePromotionConverter.getRuleRequest();
        itemController.savePromotion(ruleRequest);
    }

    private void saveItems() {
        ItemSaveRequest itemSaveRequest = resourceItemConverter.getItemSaveRequest();
        itemController.saveItem(itemSaveRequest);
    }
}
