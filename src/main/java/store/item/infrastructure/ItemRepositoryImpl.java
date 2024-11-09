package store.item.infrastructure;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import store.item.domain.item.Item;
import store.item.service.repository.ItemRepository;
import store.item.service.repository.PromotionItemRepository;

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

    @Override
    public Optional<Item> findByName(String name) {
        return Optional.ofNullable(itemMap.get(name));
    }

    @Override
    public Item delete(String id) {
        if (id == null) {
            throw new IllegalArgumentException("id cannot be null");
        }
        return itemMap.remove(id);
    }

    @Override
    public List<Item> findAll() {
        return new ArrayList<>(itemMap.values());
    }
}
