package store.item.service;

public interface MapRepository<K, V> {

    V save(K id, V value);

    V findById(K id);

    V update(K id, V value);
}
