package store.purchase.service.factory.promotion;

import java.util.ArrayList;
import java.util.List;
import store.purchase.controller.dto.response.PromotionConflict;
import store.purchase.controller.dto.response.PromotionConflictResponse;
import store.purchase.domain.Purchase;
import store.purchase.domain.discount.promotion.PromotionDiscountInfo;

public class PromotionDiscountConflictFactory {

    public PromotionConflictResponse getPromotionConflicts(Purchase purchase) {
        List<PromotionDiscountInfo> promotionDiscountsInfos =
                purchase.getPromotionDiscount().getPromotionDiscountsInfos();
        List<PromotionConflict> conflicts = new ArrayList<>();

        for (PromotionDiscountInfo promotionDiscountInfo : promotionDiscountsInfos) {
            conflicts.add(PromotionConflict.create(promotionDiscountInfo));

        }
        return new PromotionConflictResponse(conflicts);
    }
}
