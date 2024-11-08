package store.item.domain.item;

import java.util.Date;
import store.common.util.IdHolder;
import store.item.controller.dto.request.ItemRequest;

public class PromotionItem {
    private final String promotionItemId;
    private final String itemId;
    private final String name;
    private final PromotionRule promotionRule;
    private final long promotionStockQuantity;

    private PromotionItem(String promotionItemId, String itemId, String name, PromotionRule promotionRule,
                          long promotionStockQuantity) {
        this.promotionItemId = promotionItemId;
        this.itemId = itemId;
        this.name = name;
        this.promotionRule = promotionRule;
        this.promotionStockQuantity = promotionStockQuantity;
    }

    public static PromotionItem create(IdHolder idHolder, Item item, ItemRequest itemRequest) {
        if (!itemRequest.isPromotionRuleExist()) {
            throw new IllegalArgumentException("not a promotion Item");
        }
        return new PromotionItem(idHolder.generateId(), item.getId(), itemRequest.getName(),
                itemRequest.getPromotionRule().get(), itemRequest.getQuantity());
    }

    public PromotionItem purchase(long quantity) {
        if (promotionStockQuantity < quantity) {
            throw new IllegalArgumentException();
        }
        return new PromotionItem(promotionItemId, itemId, name, promotionRule, promotionStockQuantity - quantity);
    }

    public String getPromotionItemId() {
        return promotionItemId;
    }

    public String getItemId() {
        return itemId;
    }

    public String getName() {
        return name;
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
