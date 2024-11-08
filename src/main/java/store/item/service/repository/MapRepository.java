package store.item.service.repository;

import java.util.Optional;

public interface MapRepository<K, V> {

    V save(K id, V value);

    V findById(K id);

    V update(K id, V value);

    Optional<V> findByName(String name);

    V delete(K id);
}
