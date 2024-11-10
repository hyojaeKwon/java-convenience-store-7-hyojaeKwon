package store.item.infrastructure;

import static store.common.exception.repository.RepositoryException.ID_NULL;
import static store.common.exception.repository.RepositoryException.NOT_FOUND;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import store.common.exception.repository.RepositoryException;
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
        idValueNullCheck(id, value);
        itemMap.put(id, value);
        return value;
    }

    @Override
    public Item findById(String id) {
        idNullCheck(id);
        return Optional.ofNullable(itemMap.get(id)).orElseThrow(() -> new RepositoryException(NOT_FOUND));
    }

    @Override
    public Item update(String id, Item value) {
        idValueNullCheck(id, value);
        this.save(id, value);
        return itemMap.get(id);
    }

    @Override
    public Optional<Item> findByName(String name) {
        return itemMap.values().stream().filter(item -> item.getName().equals(name)).findFirst();
    }

    @Override
    public Item delete(String id) {
        idNullCheck(id);
        return itemMap.remove(id);
    }

    @Override
    public List<Item> findAll() {
        return new ArrayList<>(itemMap.values());
    }

    private void idNullCheck(String id) {
        if (id == null) {
            throw new RepositoryException(ID_NULL);
        }
    }

    private void idValueNullCheck(String id, Item value) {
        if (id == null || value == null) {
            throw new RepositoryException(ID_NULL);
        }
    }
}
