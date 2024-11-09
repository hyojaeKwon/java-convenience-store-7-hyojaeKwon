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

    public ItemInfo purchase(long amount) {
        if (!canPurchase(amount)) {
            throw new IllegalArgumentException("구매 수량 초과");
        }
        if (isPromotion) {
            if (amount > promotionItem.getStockQuantity()) {
                return purchaseWhenAmountIsBiggerThanPromotion(amount);
            }
            return purchaseWhenAmountIsSameAndLessThanPromotion(amount);
        }
        return purchaseGeneralItem(amount);
    }

    private ItemInfo purchaseWhenAmountIsBiggerThanPromotion(long amount) {
        long purchasePromotionAmount = promotionItem.getStockQuantity();
        long purchaseGeneralAmount = amount - purchasePromotionAmount;
        Item purchasedItem = item.purchase(purchaseGeneralAmount);
        PromotionItem purchasedPromotionItem = promotionItem.purchase(purchasePromotionAmount);
        return new ItemInfo(purchasedItem, purchasedPromotionItem, false);
    }

    private ItemInfo purchaseWhenAmountIsSameAndLessThanPromotion(long amount) {
        PromotionItem purchasedPromotionItem = promotionItem.purchase(amount);
        if (purchasedPromotionItem.getStockQuantity() == 0) {
            return new ItemInfo(item, purchasedPromotionItem, false);
        }
        return new ItemInfo(item, purchasedPromotionItem, true);
    }

    private ItemInfo purchaseGeneralItem(long amount) {
        Item purchased = item.purchase(amount);
        return new ItemInfo(purchased, promotionItem, false);
    }

    public boolean canPurchase(long amount) {
        return amount <= (item.getStockQuantity() + promotionItem.getStockQuantity());
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

    public boolean isPromotion() {
        return isPromotion;
    }
    // 만약 프로모션이면 상품을 몇개 구매할 수 있는지
}
