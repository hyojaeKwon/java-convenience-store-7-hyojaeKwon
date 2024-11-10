package store.item.infrastructure;

import static store.common.exception.repository.RepositoryException.ID_NULL;
import static store.common.exception.repository.RepositoryException.NOT_FOUND;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import store.common.exception.repository.RepositoryException;
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
        idValueNullCheck(id, value);

        promotionItemMap.put(id, value);
        return value;
    }

    @Override
    public PromotionItem findById(String id) {
        idNullCheck(id);
        return Optional.ofNullable(promotionItemMap.get(id)).orElseThrow(() -> new RepositoryException(NOT_FOUND));
    }

    @Override
    public PromotionItem update(String id, PromotionItem value) {
        this.save(id, value);
        return promotionItemMap.get(id);
    }

    @Override
    public Optional<PromotionItem> findByName(String name) {
        idNullCheck(name);
        return promotionItemMap.values().stream().filter(item -> item.getName().equals(name)).findFirst();
    }

    @Override
    public PromotionItem delete(String id) {
        idNullCheck(id);
        return promotionItemMap.remove(id);
    }

    @Override
    public List<PromotionItem> findAll() {
        return new ArrayList<>(promotionItemMap.values());
    }

    private void idNullCheck(String id) {
        if (id == null) {
            throw new RepositoryException(ID_NULL);
        }
    }

    private void idValueNullCheck(String id, PromotionItem value) {
        if (id == null || value == null) {
            throw new RepositoryException(ID_NULL);
        }
    }
}
