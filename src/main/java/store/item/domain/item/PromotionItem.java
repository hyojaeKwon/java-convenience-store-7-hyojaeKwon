package store.item.domain.item;

import java.util.Date;

public class PromotionItem {
    private final String promotionItemId;
    private final String itemId;
    private final PromotionRule promotionRule;
    private final long promotionStockQuantity;

    public PromotionItem(String promotionItemId, String itemId, PromotionRule promotionRule, long promotionStockQuantity) {
        this.promotionItemId = promotionItemId;
        this.itemId = itemId;
        this.promotionRule = promotionRule;
        this.promotionStockQuantity = promotionStockQuantity;
    }

    public String getPromotionItemId() {
        return promotionItemId;
    }

    public String getItemId() {
        return itemId;
    }

    public PromotionRule getPromotionRule() {
        return promotionRule;
    }

    public long getPromotionStockQuantity() {
        return promotionStockQuantity;
    }

    public long getPromotionQuantity(long buyQuantityInput) {
        return promotionRule.getPromotionQuantity(buyQuantityInput);
    }

    public boolean isActive(Date nowDate) {
        return promotionRule.isPromotionDate(nowDate);
    }
}
