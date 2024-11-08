package store.purchase.domain;

import store.purchase.domain.discount.membership.MembershipDiscount;
import store.purchase.domain.discount.promotion.PromotionDiscount;
import store.purchase.domain.value.PurchaseItems;

public class Purchase {
    private final PurchaseItems purchaseItems;
    private final PromotionDiscount promotionDiscount;
    private final MembershipDiscount membershipDiscount;

    public Purchase(PurchaseItems purchaseItems, PromotionDiscount promotionDiscount,
                    MembershipDiscount membershipDiscount) {
        this.purchaseItems = purchaseItems;
        this.promotionDiscount = promotionDiscount;
        this.membershipDiscount = membershipDiscount;
    }

    public PurchaseItems getPurchaseItems() {
        return purchaseItems;
    }

    public PromotionDiscount getPromotionDiscount() {
        return promotionDiscount;
    }

    public MembershipDiscount getMembershipDiscount() {
        return membershipDiscount;
    }

    public Purchase applyPromotionDiscount(PromotionDiscount promotionDiscountInput) {
        return new Purchase(purchaseItems, promotionDiscountInput, membershipDiscount);
    }
}
