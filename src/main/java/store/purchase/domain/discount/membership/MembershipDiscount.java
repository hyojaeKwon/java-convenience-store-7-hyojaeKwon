package store.purchase.domain.discount.membership;

public class MembershipDiscount {

    private final boolean discountFlag;
    private final long discountAmount;

    private MembershipDiscount(boolean discountFlag, long discountAmount) {
        this.discountFlag = discountFlag;
        this.discountAmount = discountAmount;
    }

    public static MembershipDiscount createMembershipDiscount(long discountAmount) {
        if (discountAmount <= 0) {
            throw new IllegalArgumentException("discount amount must be greater than zero");
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
