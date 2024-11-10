package store.purchase.controller.dto.request.promotion;

import store.purchase.controller.dto.response.PromotionConflict;
import store.purchase.domain.discount.promotion.PromotionDiscountStatus;

public class ResolveRequest {

    private final String name;
    private final PromotionDiscountStatus status;
    private final long quantity;
    private final boolean solve;

    private ResolveRequest(String name, PromotionDiscountStatus status, long quantity, boolean solve) {
        this.name = name;
        this.status = status;
        this.quantity = quantity;
        this.solve = solve;
    }

    public static ResolveRequest of(PromotionConflict response, boolean solve) {
        return new ResolveRequest(response.getName(), response.getStatus(), response.getQuantity(), solve);
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

    public boolean isSolve() {
        return solve;
    }
}
