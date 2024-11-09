package store.item.service.repository;

import java.util.List;
import store.item.domain.item.Item;

public interface ItemRepository extends MapRepository<String, Item> {
    List<Item> findAll();
}
