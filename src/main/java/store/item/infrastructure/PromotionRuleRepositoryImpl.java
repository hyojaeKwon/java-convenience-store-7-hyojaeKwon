package store.item.infrastructure;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
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
        try{
            return ruleMap.put(id, value);
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public PromotionRule findById(String id) {
        return ruleMap.get(id);
    }

    @Override
    public PromotionRule update(String id, PromotionRule value) {
        return ruleMap.put(id, value);
    }

    @Override
    public Optional<PromotionRule> findByName(String name) {
        return Optional.ofNullable(ruleMap.get(name));
    }

    @Override
    public PromotionRule delete(String id) {
        return ruleMap.remove(id);
    }
}
