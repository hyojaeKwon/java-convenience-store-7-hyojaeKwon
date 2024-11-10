package store.purchase.controller.dto.response;

import store.purchase.domain.discount.promotion.PromotionDiscountInfo;
import store.purchase.domain.discount.promotion.PromotionDiscountStatus;

public class PromotionConflict {
    private final String name;
    private final PromotionDiscountStatus status;
    private final long quantity;

    private PromotionConflict(String name, PromotionDiscountStatus status, long quantity) {
        this.name = name;
        this.status = status;
        this.quantity = quantity;
    }

    public static PromotionConflict create(PromotionDiscountInfo discountInfo){
        return new PromotionConflict(discountInfo.getName(), discountInfo.getStatus(),
                discountInfo.getStatusQuantity());
    }

    public String getName() {
        return name;
    }

    public PromotionDiscountStatus getStatus() {
        return status;
    }

    public long getQuantity() {
        return quantity;
    }
}
