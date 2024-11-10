package store.purchase.service;

import java.util.List;
import store.purchase.controller.PurchaseDiscountService;
import store.purchase.domain.Purchase;
import store.purchase.domain.discount.membership.MembershipDiscount;
import store.purchase.domain.discount.promotion.PromotionDiscount;
import store.purchase.domain.discount.promotion.PromotionDiscountInfo;
import store.purchase.domain.value.PurchaseItem;
import store.purchase.service.factory.promotion.PromotionDiscountInfoFactory;

public class PurchaseDiscountServiceImpl implements PurchaseDiscountService {

    public static final int MEMBERSHIP_MAX_DISCOUNT_AMOUNT = 8000;
    public static final int MEMBERSHIP_DISCOUNT_PERCENT = 30;
    private final PromotionDiscountInfoFactory discountInfoFactory;

    public PurchaseDiscountServiceImpl(PromotionDiscountInfoFactory discountInfoFactory) {
        this.discountInfoFactory = discountInfoFactory;
    }

    @Override
    public Purchase applyMembershipDiscount(Purchase purchase) {

        long promotionDiscountSum = purchase.getPromotionDiscount().sumOfDiscountAmount();
        long purchaseSum = purchase.getPurchaseSum();
        long memberShipDiscountAmount = (purchaseSum - promotionDiscountSum) * MEMBERSHIP_DISCOUNT_PERCENT / 100;

        MembershipDiscount memberShipDiscount = membershipDiscountCreate(memberShipDiscountAmount);
        return purchase.applyMembershipDiscount(memberShipDiscount);
    }

    private MembershipDiscount membershipDiscountCreate(long memberShipDiscountAmount) {
        if (memberShipDiscountAmount > MEMBERSHIP_MAX_DISCOUNT_AMOUNT) {
            memberShipDiscountAmount = MEMBERSHIP_MAX_DISCOUNT_AMOUNT;
        }
        return MembershipDiscount.createMembershipDiscount(memberShipDiscountAmount);
    }

    @Override
    public Purchase applyPromotionDiscount(Purchase purchase) {
        List<PromotionDiscountInfo> discountInfos = purchase.getPurchaseItems().stream()
                .filter(PurchaseItem::isPromotion).map(discountInfoFactory::generatePromotionDiscountInfo).toList();
        PromotionDiscount promotionDiscount = new PromotionDiscount(discountInfos);
        return purchase.applyPromotionDiscount(promotionDiscount);
    }
}


