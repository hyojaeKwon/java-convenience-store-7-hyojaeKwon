package store.purchase.domain.discount.membership;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.common.exception.domain.DomainStateException;
import store.purchase.domain.discount.membership.MembershipDiscount;

class MembershipDiscountTest {

    @Test
    @DisplayName("MembershipDiscount 객체 생성을 성공한다.")
    void testCreateValidMembershipDiscount() {
        MembershipDiscount discount = MembershipDiscount.createMembershipDiscount(50);

        Assertions.assertThat(discount.getDiscountAmount()).isEqualTo(50);
    }

    @Test
    @DisplayName("할인 금액이 0 이하일 경우 예외를 반환한다.")
    void testCreateMembershipDiscountWithInvalidAmount() {
        Assertions.assertThatThrownBy(() -> MembershipDiscount.createMembershipDiscount(0))
                .isInstanceOf(IllegalStateException.class);

        Assertions.assertThatThrownBy(() -> MembershipDiscount.createMembershipDiscount(-10))
                .isInstanceOf(IllegalStateException.class);
    }
}