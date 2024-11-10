package store.purchase.domain;

import store.purchase.controller.dto.request.purchase.PurchaseItemRequest;

public class PurchaseCreate {
    private final String name;
    private final boolean isPromotion;
    private final long quantity;
    private final long price;

    private PurchaseCreate(String name, long quantity, long price, boolean isPromotion) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.isPromotion = isPromotion;
    }

    public static PurchaseCreate create(PurchaseItemRequest request, long price, boolean isPromotion) {
        PurchaseCreate purchaseCreate = new PurchaseCreate(request.getName(), request.getQuantity(), price,
                isPromotion);
        validation(purchaseCreate);
        return purchaseCreate;
    }

    private static void validation(PurchaseCreate purchaseCreate) {
        if (purchaseCreate.getQuantity() <= 0) {
            throw new IllegalArgumentException();
        }
        if (purchaseCreate.getPrice() < 0) {
            throw new IllegalArgumentException();
        }
    }

    public String getName() {
        return name;
    }

    public long getQuantity() {
        return quantity;
    }

    public long getPrice() {
        return price;
    }

    public boolean isPromotion() {
        return isPromotion;
    }
}