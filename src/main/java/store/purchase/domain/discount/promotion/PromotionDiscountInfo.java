package store.purchase.domain.discount.promotion;

import static store.purchase.domain.discount.promotion.PromotionDiscountStatus.CANT_DISCOUNT_ALL;
import static store.purchase.domain.discount.promotion.PromotionDiscountStatus.GET_MORE;
import static store.purchase.domain.discount.promotion.PromotionDiscountStatus.RESOLVED;

public class PromotionDiscountInfo {

    private final PromotionDiscountItem promotionDiscountItem;
    private final PromotionDiscountStatus status;
    private final long statusQuantity;

    private PromotionDiscountInfo(PromotionDiscountItem promotionDiscountItem, PromotionDiscountStatus status,
                                  long statusQuantity) {
        this.promotionDiscountItem = promotionDiscountItem;
        this.status = status;
        this.statusQuantity = statusQuantity;
    }

    public static PromotionDiscountInfo create(PromotionDiscountItem promotionDiscountItem) {
        return new PromotionDiscountInfo(promotionDiscountItem, RESOLVED, 0);
    }

    public static PromotionDiscountInfo createCanGetMore(PromotionDiscountItem promotionDiscountItem, long quantity) {
        return new PromotionDiscountInfo(promotionDiscountItem, GET_MORE, quantity);
    }

    public static PromotionDiscountInfo createCannotDiscountAll(PromotionDiscountItem promotionDiscountItem,
                                                                long quantity) {
        return new PromotionDiscountInfo(promotionDiscountItem, CANT_DISCOUNT_ALL, quantity);
    }

    public PromotionDiscountInfo resolveCanGetMore() {
        PromotionDiscountItem discountItem = new PromotionDiscountItem(promotionDiscountItem.getName(),
                promotionDiscountItem.getPrice(), promotionDiscountItem.getBuyAmount() + 1,
                promotionDiscountItem.getDiscountAmount() + 1);
        return new PromotionDiscountInfo(discountItem, RESOLVED, statusQuantity);
    }

    public PromotionDiscountInfo resolveDiscountConflict() {
        return new PromotionDiscountInfo(promotionDiscountItem, RESOLVED, 0);
    }

    public long getStatusQuantity() {
        return statusQuantity;
    }

    public PromotionDiscountItem getPromotionDiscountItem() {
        return promotionDiscountItem;
    }

    public PromotionDiscountStatus getStatus() {
        return status;
    }

    public String getName() {
        return promotionDiscountItem.getName();
    }
}