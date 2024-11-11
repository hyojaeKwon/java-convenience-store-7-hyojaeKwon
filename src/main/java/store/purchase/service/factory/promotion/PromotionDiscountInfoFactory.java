package store.purchase.service.factory.promotion;

import store.item.controller.ItemStockService;
import store.item.domain.item.value.ItemInfo;
import store.purchase.domain.discount.promotion.PromotionDiscountInfo;
import store.purchase.domain.discount.promotion.PromotionDiscountItem;
import store.purchase.domain.value.PurchaseItem;

public class PromotionDiscountInfoFactory {

    private final ItemStockService itemStockService;

    public PromotionDiscountInfoFactory(ItemStockService itemStockService) {
        this.itemStockService = itemStockService;
    }

    public PromotionDiscountInfo generatePromotionDiscountInfo(PurchaseItem purchaseItem) {
        ItemInfo itemInfo = itemStockService.getItemInfoByName(purchaseItem.getName());
        long purchaseItemAmount = purchaseItem.getAmount();
        long discountAmount = itemInfo.judgePromotionAmount(purchaseItemAmount);

        PromotionDiscountItem promotionDiscountItem = new PromotionDiscountItem(purchaseItem.getName(),
                purchaseItem.getPrice(), purchaseItemAmount, discountAmount);
        return getPromotionDiscountInfo(itemInfo, purchaseItemAmount, promotionDiscountItem);
    }

    private PromotionDiscountInfo getPromotionDiscountInfo(ItemInfo itemInfo, long purchaseItemAmount,
                                                           PromotionDiscountItem promotionDiscountItem) {
        if (itemInfo.getPromotionItem().getStockQuantity() > purchaseItemAmount && (itemInfo.judgeGetMorePromotion(
                purchaseItemAmount))) {
            return PromotionDiscountInfo.createCanGetMore(promotionDiscountItem, 1);
        }
        return getCannotDiscountInfo(itemInfo, purchaseItemAmount, promotionDiscountItem);
    }

    private PromotionDiscountInfo getCannotDiscountInfo(ItemInfo itemInfo, long purchaseItemAmount,
                                                        PromotionDiscountItem promotionDiscountItem) {
        long cannotDiscountAmount = itemInfo.judgeCannotPromotionAmount(purchaseItemAmount);
        return judgeDiscountAmount(promotionDiscountItem, cannotDiscountAmount);
    }

    private PromotionDiscountInfo judgeDiscountAmount(PromotionDiscountItem promotionDiscountItem,
                                                      long cannotDiscountAmount) {
        if (cannotDiscountAmount == 0) {
            return PromotionDiscountInfo.create(promotionDiscountItem);
        }
        return PromotionDiscountInfo.createCannotDiscountAll(promotionDiscountItem, cannotDiscountAmount);
    }
}
