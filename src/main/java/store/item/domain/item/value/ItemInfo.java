package store.item.domain.item.value;

import static store.common.exception.domain.DomainArgumentException.EXCEED_AMOUNT;
import static store.common.exception.domain.DomainStateException.NOT_PROMOTION;

import store.common.exception.domain.DomainArgumentException;
import store.common.exception.domain.DomainStateException;
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
            throw new DomainArgumentException(EXCEED_AMOUNT);
        }
        if (isPromotion) {
            return handlePromotionPurchase(amount);
        } else {
            return purchaseGeneralItem(amount);
        }
    }

    private ItemInfo handlePromotionPurchase(long amount) {
        if (amount > promotionItem.getStockQuantity()) {
            return purchaseWhenAmountIsBiggerThanPromotion(amount);
        }
        return purchaseWhenAmountIsSameAndLessThanPromotion(amount);
    }

    private ItemInfo purchaseWhenAmountIsBiggerThanPromotion(long amount) {
        long purchasePromotionAmount = promotionItem.getStockQuantity();
        long purchaseGeneralAmount = amount - purchasePromotionAmount;
        Item purchasedItem = item.purchase(purchaseGeneralAmount);
        PromotionItem purchasedPromotionItem = promotionItem.purchase(purchasePromotionAmount);
        return new ItemInfo(purchasedItem, purchasedPromotionItem, isPromotion);
    }

    private ItemInfo purchaseWhenAmountIsSameAndLessThanPromotion(long amount) {
        PromotionItem purchasedPromotionItem = promotionItem.purchase(amount);
        return new ItemInfo(item, purchasedPromotionItem, isPromotion);
    }

    private ItemInfo purchaseGeneralItem(long amount) {
        Item purchased = item.purchase(amount);
        return new ItemInfo(purchased, promotionItem, false);
    }

    public boolean canPurchase(long amount) {
        if (isPromotion) {
            return amount <= (item.getStockQuantity() + promotionItem.getStockQuantity());
        }
        return amount <= item.getStockQuantity();
    }

    public PromotionItem getPromotionItem() {
        checkPromotion();
        return promotionItem;
    }

    public long judgePromotionAmount(long buyAmount) {
        checkPromotion();
        if (buyAmount > promotionItem.getStockQuantity()) {
            return promotionItem.getPromotionQuantity(promotionItem.getStockQuantity());
        }
        return promotionItem.getPromotionQuantity(buyAmount);
    }

    public long judgeCannotPromotionAmount(long buyAmount) {
        checkPromotion();
        return buyAmount - (judgePromotionAmount(buyAmount) * promotionItem.getPromotionRuleQuantitySum());
    }

    public boolean judgeGetMorePromotion(long buyAmount) {
        checkPromotion();
        return promotionItem.judgeCanGetOneMore(buyAmount);
    }

    private void checkPromotion() {
        if (!isPromotion) {
            throw new DomainStateException(NOT_PROMOTION);
        }
    }

    public Item getItem() {
        return item;
    }

    public boolean isPromotion() {
        return isPromotion;
    }

    public long getPrice() {
        return item.getPrice();
    }
}