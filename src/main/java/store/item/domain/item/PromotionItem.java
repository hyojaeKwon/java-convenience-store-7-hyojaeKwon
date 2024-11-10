package store.item.domain.item;

import java.util.Date;
import store.common.exception.domain.DomainStateException;
import store.common.util.IdHolder;
import store.item.controller.dto.request.ItemRequest;

public class PromotionItem {
    private final String promotionItemId;
    private final String name;
    private final PromotionRule promotionRule;
    private final long stockQuantity;

    private PromotionItem(String promotionItemId, String name, PromotionRule promotionRule, long stockQuantity) {
        this.promotionItemId = promotionItemId;
        this.name = name;
        this.promotionRule = promotionRule;
        this.stockQuantity = stockQuantity;
    }

    public static PromotionItem create(IdHolder idHolder, ItemRequest itemRequest,
                                       PromotionRule promotionRule) {
        if (!itemRequest.isPromotionRuleExist()) {
            throw new DomainStateException(DomainStateException.NOT_PROMOTION);
        }
        return new PromotionItem(idHolder.generateId(), itemRequest.getName(), promotionRule,
                itemRequest.getQuantity());
    }

    public PromotionItem purchase(long quantity) {
        if (stockQuantity < quantity) {
            throw new IllegalArgumentException();
        }
        return new PromotionItem(promotionItemId, name, promotionRule, stockQuantity - quantity);
    }

    public String getPromotionItemId() {
        return promotionItemId;
    }

    public String getName() {
        return name;
    }

    public long getStockQuantity() {
        return stockQuantity;
    }

    public long getPromotionRuleQuantitySum() {
        return promotionRule.getPromotionQuantitySum();
    }

    public long getPromotionQuantity(long buyQuantityInput) {
        return promotionRule.getPromotionQuantity(buyQuantityInput);
    }

    public boolean judgeCanGetOneMore(long buyQuantityInput) {
        return promotionRule.canGetOneMore(buyQuantityInput);
    }

    public PromotionRule getPromotionRule() {
        return promotionRule;
    }

    public boolean isActive(Date nowDate) {
        return promotionRule.isPromotionDate(nowDate);
    }

}
