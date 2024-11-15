package store.purchase.domain.discount.promotion;

public class PromotionDiscountItem {
    private final String name;
    private final long price;
    private final long buyAmount;
    private final long discountAmount;

    public PromotionDiscountItem(String name, long price, long buyAmount, long discountAmount) {
        this.name = name;
        this.price = price;
        this.buyAmount = buyAmount;
        this.discountAmount = discountAmount;
    }

    public long getBuyAmount() {
        return buyAmount;
    }

    public long getDiscountAmount() {
        return discountAmount;
    }

    public String getName() {
        return name;
    }

    public long getPrice() {
        return price;
    }
}
