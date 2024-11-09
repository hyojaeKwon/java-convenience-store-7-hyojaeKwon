package store.item.controller.dto.request;

import java.util.Optional;

public class ItemRequest {
    private final String name;
    private final long price;
    private final long quantity;
    private final Optional<String> promotionRule;

    public ItemRequest(String name, long price, long quantity, Optional<String> promotionRule) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.promotionRule = promotionRule;
    }

    public String getName() {
        return name;
    }

    public long getPrice() {
        return price;
    }

    public long getQuantity() {
        return quantity;
    }

    public Optional<String> getPromotionRule() {
        return promotionRule;
    }

    public boolean isPromotionRuleExist() {
        return promotionRule.isPresent();
    }
}