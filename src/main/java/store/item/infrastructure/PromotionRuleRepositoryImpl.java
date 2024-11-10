package store.item.infrastructure;

import static store.common.exception.repository.RepositoryException.ID_NULL;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import store.common.exception.repository.RepositoryException;
import store.item.domain.item.PromotionRule;
import store.item.service.repository.PromotionRuleRepository;

public class PromotionRuleRepositoryImpl implements PromotionRuleRepository {

    private static PromotionRuleRepository instance;
    private final Map<String, PromotionRule> ruleMap = new ConcurrentHashMap<>();

    private PromotionRuleRepositoryImpl() {
    }

    public static PromotionRuleRepository getInstance() {
        if (instance == null) {
            checkSyncInstance();
        }
        return instance;
    }

    private static void checkSyncInstance() {
        synchronized (PromotionRuleRepository.class) {
            if (instance == null) {
                instance = new PromotionRuleRepositoryImpl();
            }
        }
    }

    @Override
    public PromotionRule save(String id, PromotionRule value) {
        idValueNullCheck(id, value);
        return ruleMap.put(id, value);
    }

    @Override
    public PromotionRule findById(String id) {
        idNullCheck(id);
        return Optional.ofNullable(ruleMap.get(id))
                .orElseThrow(() -> new RepositoryException(RepositoryException.NOT_FOUND_PROMOTION));
    }

    @Override
    public PromotionRule update(String id, PromotionRule value) {
        save(id, value);
        return value;
    }

    @Override
    public Optional<PromotionRule> findByName(String name) {
        idNullCheck(name);
        return Optional.ofNullable(ruleMap.get(name));
    }

    @Override
    public PromotionRule delete(String id) {
        idNullCheck(id);
        return ruleMap.remove(id);
    }

    private void idNullCheck(String id) {
        if (id == null) {
            throw new RepositoryException(ID_NULL);
        }
    }

    private void idValueNullCheck(String id, PromotionRule value) {
        if (id == null || value == null) {
            throw new RepositoryException(ID_NULL);
        }
    }
}
