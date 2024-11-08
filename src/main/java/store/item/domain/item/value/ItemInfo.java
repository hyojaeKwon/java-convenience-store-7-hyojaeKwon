package store.item.domain.item.value;

import store.item.domain.item.Item;
import store.item.domain.item.PromotionItem;

public class ItemInfo {

    private final Item item;
    private final PromotionItem promotionItem;
    private final boolean isPromotion;

    private ItemInfo(Item item, PromotionItem promotionItem, boolean isPromotion) {
        this.item = item;
        this.promotionItem = promotionItem;
        this.isPromotion = isPromotion;
    }

    public static ItemInfo createPromotionItemInfo(Item item, PromotionItem promotionItem) {
        return new ItemInfo(item, promotionItem, true);
    }

    public static ItemInfo createNotPromotionItemInfo(Item item) {
        return new ItemInfo(item, null, false);
    }

    public Item getItem() {
        return item;
    }

    public PromotionItem getPromotionItem() {
        if (!isPromotion) {
            throw new IllegalArgumentException("promotion중인 상품이 아님");
        }
        return promotionItem;
    }

}
