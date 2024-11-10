package store.purchase.domain.value.receipt;

import java.util.List;
import store.purchase.domain.Purchase;
import store.purchase.domain.discount.promotion.PromotionDiscount;
import store.purchase.domain.discount.promotion.PromotionDiscountInfo;
import store.purchase.domain.discount.promotion.PromotionDiscountItem;
import store.purchase.domain.value.PurchaseItem;

public class Receipt {
    private final List<PurchaseItem> purchaseItems;
    private final PromotionDiscount promotionDiscount;
    private final ReceiptPrice price;

    private Receipt(List<PurchaseItem> purchaseItems, PromotionDiscount promotionDiscount, ReceiptPrice price) {
        this.purchaseItems = purchaseItems;
        this.promotionDiscount = promotionDiscount;
        this.price = price;
    }

    public static Receipt create(Purchase purchase) {
        ReceiptPrice price = ReceiptPrice.create(purchase);
        if (purchase.getMembershipDiscount() == null) {
            return new Receipt(purchase.getPurchaseItems(), purchase.getPromotionDiscount(), price);
        }
        return new Receipt(purchase.getPurchaseItems(), purchase.getPromotionDiscount(), price);
    }

    public List<PurchaseItem> getPurchaseItems() {
        return purchaseItems;
    }

    public List<PromotionDiscountItem> getPromotionDiscountItems() {
        return promotionDiscount.getPromotionDiscountsInfos().stream()
                .map(PromotionDiscountInfo::getPromotionDiscountItem).toList();
    }

    public ReceiptPrice getPrice() {
        return price;
    }

    public long totalPurchaseAmount() {
        return purchaseItems.stream().map(PurchaseItem::getAmount).reduce(0L, Long::sum);
    }
}
