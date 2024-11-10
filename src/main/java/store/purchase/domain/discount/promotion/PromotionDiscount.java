package store.purchase.domain.discount.promotion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PromotionDiscount {

    private final List<PromotionDiscountInfo> promotionDiscountsInfos;

    public PromotionDiscount(List<PromotionDiscountInfo> promotionDiscountsInfos) {
        this.promotionDiscountsInfos = promotionDiscountsInfos;
    }

    public List<PromotionDiscountInfo> getPromotionDiscountsInfos() {
        return Collections.unmodifiableList(promotionDiscountsInfos);
    }

    public PromotionDiscount resolveGetMoreConflict(String name) {
        List<PromotionDiscountInfo> discountInfos = promotionDiscountsInfos.stream()
                .filter(info -> info.getName().equals(name)).map(PromotionDiscountInfo::resolveCanGetMore).toList();
        return new PromotionDiscount(discountInfos);
    }

    public PromotionDiscount addPromotionDiscount(PromotionDiscountInfo promotionDiscountItem) {
        ArrayList<PromotionDiscountInfo> newPromotionDiscounts = new ArrayList<>(promotionDiscountsInfos);
        newPromotionDiscounts.add(promotionDiscountItem);
        return new PromotionDiscount(newPromotionDiscounts);
    }

    public long sumOfDiscountAmount() {
        return promotionDiscountsInfos.stream()
                .map(i -> i.getPromotionDiscountItem().getDiscountAmount() * i.getPromotionDiscountItem().getPrice())
                .reduce(0L, Long::sum);
    }

}
