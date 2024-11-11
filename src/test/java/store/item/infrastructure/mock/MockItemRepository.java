package store.item.infrastructure.mock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import store.item.domain.item.Item;
import store.item.service.repository.ItemRepository;

public class MockItemRepository implements ItemRepository {
    private final Map<String, Item> itemMap = new HashMap<>();

    @Override
    public Item save(String id, Item value) {
        return itemMap.put(id, value);
    }

    @Override
    public Item findById(String id) {
        return itemMap.get(id);
    }

    @Override
    public Item update(String id, Item value) {
        return itemMap.put(id, value);
    }

    @Override
    public Optional<Item> findByName(String name) {
        return itemMap.values().stream().filter(item -> item.getName().equals(name)).findFirst();
    }

    @Override
    public Item delete(String id) {
        return itemMap.remove(id);
    }

    @Override
    public List<Item> findAll() {
        return new ArrayList<>(itemMap.values());
    }
}
