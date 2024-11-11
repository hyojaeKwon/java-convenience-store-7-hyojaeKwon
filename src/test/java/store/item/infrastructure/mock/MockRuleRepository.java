package store.item.infrastructure.mock;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import store.item.domain.item.PromotionRule;
import store.item.service.repository.PromotionRuleRepository;

public class MockRuleRepository implements PromotionRuleRepository {
    private final Map<String, PromotionRule> ruleMap = new HashMap<>();

    @Override
    public PromotionRule save(String id, PromotionRule value) {
        return ruleMap.put(id, value);
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
