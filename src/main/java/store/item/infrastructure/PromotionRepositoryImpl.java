package store.item.infrastructure;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import store.item.domain.item.PromotionItem;
import store.item.service.repository.PromotionItemRepository;

public class PromotionRepositoryImpl implements PromotionItemRepository {

    private static PromotionItemRepository instance;
    private final Map<String, PromotionItem> promotionItemMap = new ConcurrentHashMap<>();

    private PromotionRepositoryImpl() {
    }

    public static PromotionItemRepository getInstance() {
        if (instance == null) {
            checkSyncInstance();
        }
        return instance;
    }

    private static void checkSyncInstance() {
        synchronized (PromotionRepositoryImpl.class) {
            if (instance == null) {
                instance = new PromotionRepositoryImpl();
            }
        }
    }

    @Override
    public PromotionItem save(String id, PromotionItem value) {
        if (id == null || value == null) {
            throw new IllegalArgumentException("id and value can't be null");
        }

        promotionItemMap.put(id, value);
        return value;
    }

    @Override
    public PromotionItem findById(String id) {
        if (id == null) {
            throw new IllegalArgumentException("id can't be null");
        }
        return Optional.ofNullable(promotionItemMap.get(id))
                .orElseThrow(() -> new IllegalArgumentException("Promotion item with id " + id + " not found"));
    }

    @Override
    public PromotionItem update(String id, PromotionItem value) {
        this.save(id, value);
        return promotionItemMap.get(id);
    }

    @Override
    public Optional<PromotionItem> findByName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("name can't be null");
        }
        return promotionItemMap.values().stream().filter(item -> item.getName().equals(name)).findFirst();
    }

    @Override
    public PromotionItem delete(String id) {
        if (id == null) {
            throw new IllegalArgumentException("id can't be null");
        }
        return promotionItemMap.remove(id);
    }

    @Override
    public List<PromotionItem> findAll() {
        return new ArrayList<>(promotionItemMap.values());
    }
}
