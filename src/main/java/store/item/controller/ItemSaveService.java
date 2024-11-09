package store.item.controller;

import java.util.List;
import store.item.controller.dto.request.ItemSaveRequest;
import store.item.controller.dto.request.PromotionRuleRequest;

public interface ItemSaveService {

    void saveRule(List<PromotionRuleRequest> request);

    void save(ItemSaveRequest itemSaveRequest);
}
