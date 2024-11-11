package store.purchase.domain.discount.membership;

import static store.common.exception.domain.DomainStateException.MEMBERSHIP_DISCOUNT_BOUND;

import store.common.exception.domain.DomainStateException;

public class MembershipDiscount {

    private final boolean discountFlag;
    private final long discountAmount;

    private MembershipDiscount(boolean discountFlag, long discountAmount) {
        this.discountFlag = discountFlag;
        this.discountAmount = discountAmount;
    }

    public static MembershipDiscount createMembershipDiscount(long discountAmount) {
        if (discountAmount <= 0) {
            throw new DomainStateException(MEMBERSHIP_DISCOUNT_BOUND);
        }
        return new MembershipDiscount(true, discountAmount);
    }

    public boolean isDiscountFlag() {
        return discountFlag;
    }

    public long getDiscountAmount() {
        return discountAmount;
    }
}
