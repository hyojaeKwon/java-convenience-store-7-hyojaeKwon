package store.purchase.domain.value.receipt;

import java.util.List;
import store.purchase.domain.Purchase;
import store.purchase.domain.discount.membership.MembershipDiscount;
import store.purchase.domain.discount.promotion.PromotionDiscount;
import store.purchase.domain.value.PurchaseItem;

public class ReceiptPrice {
    private final long totalPrice;
    private final long promotionDiscountPrice;
    private final long membershipDiscountPrice;
    private final long resultPrice;

    private ReceiptPrice(long totalPrice, long promotionDiscountPrice, long membershipDiscountPrice, long resultPrice) {
        this.totalPrice = totalPrice;
        this.promotionDiscountPrice = promotionDiscountPrice;
        this.membershipDiscountPrice = membershipDiscountPrice;
        this.resultPrice = resultPrice;
    }

    public static ReceiptPrice create(Purchase purchase) {
        long totalPrice = getTotalPrice(purchase.getPurchaseItems());
        long promotion = getDiscountPrice(purchase.getPromotionDiscount());
        long membership = getMembershipDiscountPrice(purchase.getMembershipDiscount());
        long resultPrice = totalPrice - promotion - membership;

        return new ReceiptPrice(totalPrice, promotion, membership, resultPrice);
    }

    public static long getMembershipDiscountPrice(MembershipDiscount membershipDiscount) {
        if (membershipDiscount == null) {
            return 0;
        }
        return membershipDiscount.getDiscountAmount();
    }
    public static long getTotalPrice(List<PurchaseItem> purchaseItems) {
        return purchaseItems.stream().map(PurchaseItem::getTotalPrice).reduce(0L, Long::sum);
    }

    public static long getDiscountPrice(PromotionDiscount promotionDiscount) {
        return promotionDiscount.sumOfDiscountAmount();
    }

    public long getTotalPrice() {
        return totalPrice;
    }

    public long getPromotionDiscountPrice() {
        return promotionDiscountPrice;
    }

    public long getMembershipDiscountPrice() {
        return membershipDiscountPrice;
    }

    public long getResultPrice() {
        return resultPrice;
    }
}
