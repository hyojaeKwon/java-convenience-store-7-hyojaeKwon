package store.purchase.domain.discount.promotion;

public class PromotionDiscountInfo {
    private final PromotionDiscountItem promotionDiscountItem;
    private final PromotionDiscountStatus status;
    private final long quantity;

    private PromotionDiscountInfo(PromotionDiscountItem promotionDiscountItem, PromotionDiscountStatus status,
                                  long quantity) {
        this.promotionDiscountItem = promotionDiscountItem;
        this.status = status;
        this.quantity = quantity;
    }

    public static PromotionDiscountInfo create(PromotionDiscountItem promotionDiscountItem) {
        return new PromotionDiscountInfo(promotionDiscountItem, PromotionDiscountStatus.RESOLVED, 0);
    }

    public static PromotionDiscountInfo createCanGetMore(PromotionDiscountItem promotionDiscountItem, long quantity) {
        return new PromotionDiscountInfo(promotionDiscountItem, PromotionDiscountStatus.GET_MORE, quantity);
    }

    public static PromotionDiscountInfo createCannotDiscountAll(PromotionDiscountItem promotionDiscountItem,
                                                                long quantity) {
        return new PromotionDiscountInfo(promotionDiscountItem, PromotionDiscountStatus.CANT_DISCOUNT_ALL, quantity);
    }

    public PromotionDiscountInfo resolveDiscountConflict() {
        return new PromotionDiscountInfo(promotionDiscountItem, PromotionDiscountStatus.RESOLVED, 0);
    }

    public long getQuantity() {
        return quantity;
    }

    public PromotionDiscountItem getPromotionDiscountItem() {
        return promotionDiscountItem;
    }

    public PromotionDiscountStatus getStatus() {
        return status;
    }
}