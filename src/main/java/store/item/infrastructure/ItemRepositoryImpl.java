package store.item.infrastructure;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import store.item.domain.item.Item;
import store.item.service.ItemRepository;
import store.item.service.PromotionItemRepository;

public class ItemRepositoryImpl implements ItemRepository {

    private static ItemRepository instance;
    private final Map<String, Item> itemMap = new ConcurrentHashMap<>();

    private ItemRepositoryImpl() {
    }

    public static ItemRepository getInstance() {
        if (instance == null) {
            checkSyncInstance();
        }
        return instance;
    }

    private static void checkSyncInstance() {
        synchronized (PromotionItemRepository.class) {
            if (instance == null) {
                instance = new ItemRepositoryImpl();
            }
        }
    }

    @Override
    public Item save(String id, Item value) {
        if (id == null || value == null) {
            throw new IllegalArgumentException("id and value cannot be null");
        }

        itemMap.put(id, value);
        return value;
    }

    @Override
    public Item findById(String id) {
        if (id == null) {
            throw new IllegalArgumentException("id cannot be null");
        }
        return Optional.ofNullable(itemMap.get(id)).orElseThrow(() -> new IllegalArgumentException("Item not found"));
    }

    @Override
    public Item update(String id, Item value) {
        this.save(id, value);
        return itemMap.get(id);
    }
}
