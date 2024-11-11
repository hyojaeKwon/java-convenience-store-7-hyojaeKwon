package store.purchase.domain.value;

import store.purchase.domain.PurchaseCreate;

public class PurchaseItem {
    private final String name;
    private final boolean isPromotion;
    private final long amount;
    private final long price;
    private final long totalPrice;

    private PurchaseItem(String name, boolean isPromotion, long amount, long price, long totalPrice) {
        this.name = name;
        this.isPromotion = isPromotion;
        this.amount = amount;
        this.price = price;
        this.totalPrice = totalPrice;
    }

    public static PurchaseItem create(PurchaseCreate purchaseCreate) {
        return new PurchaseItem(purchaseCreate.getName(), purchaseCreate.isPromotion(), purchaseCreate.getQuantity(),
                purchaseCreate.getPrice(), purchaseCreate.getPrice() * purchaseCreate.getQuantity());
    }

    public PurchaseItem addPurchaseAmount(long addAmount) {
        return new PurchaseItem(name, isPromotion, amount + addAmount, price, totalPrice + addAmount * price);
    }

    public long getTotalPrice() {
        return totalPrice;
    }

    public String getName() {
        return name;
    }

    public boolean isPromotion() {
        return isPromotion;
    }

    public long getAmount() {
        return amount;
    }

    public long getPrice() {
        return price;
    }
}
