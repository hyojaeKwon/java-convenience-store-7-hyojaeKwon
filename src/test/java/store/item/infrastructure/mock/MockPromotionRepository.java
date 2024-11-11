package store.item.infrastructure.mock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import store.item.domain.item.PromotionItem;
import store.item.service.repository.PromotionItemRepository;

public class MockPromotionRepository implements PromotionItemRepository {
    private final Map<String, PromotionItem> promotionItemMap = new HashMap<>();

    @Override
    public PromotionItem save(String id, PromotionItem value) {
        return promotionItemMap.put(id, value);
    }

    @Override
    public PromotionItem findById(String id) {
        return promotionItemMap.get(id);
    }

    @Override
    public PromotionItem update(String id, PromotionItem value) {
        return promotionItemMap.put(id, value);
    }

    @Override
    public Optional<PromotionItem> findByName(String name) {
        return promotionItemMap.values().stream().filter(item -> item.getName().equals(name)).findFirst();
    }

    @Override
    public PromotionItem delete(String id) {
        return promotionItemMap.remove(id);
    }

    @Override
    public List<PromotionItem> findAll() {
        return new ArrayList<>(promotionItemMap.values());
    }
}
