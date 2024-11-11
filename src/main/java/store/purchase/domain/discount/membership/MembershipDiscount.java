package store.purchase.domain.discount.membership;

import static store.common.exception.domain.DomainStateException.MEMBERSHIP_DISCOUNT_BOUND;

import store.common.exception.domain.DomainStateException;

public class MembershipDiscount {

    private final long discountAmount;

    private MembershipDiscount(long discountAmount) {
        this.discountAmount = discountAmount;
    }

    public static MembershipDiscount createMembershipDiscount(long discountAmount) {
        if (discountAmount <= 0) {
            throw new DomainStateException(MEMBERSHIP_DISCOUNT_BOUND);
        }
        return new MembershipDiscount(discountAmount);
    }

    public long getDiscountAmount() {
        return discountAmount;
    }
}
