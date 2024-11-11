package store.common.exception.domain;

public class DomainStateException extends IllegalStateException {
    public static final String NOT_PROMOTION = "프로모션 진행 중인 상품이 아닙니다.";
    public static final String PRODUCTION_PRICE_NEG = "상품 가격은 음수일 수 없습니다.";
    // purchase
    public static final String MEMBERSHIP_DISCOUNT_BOUND = "할인 금액은 음수일 수 없습니다";

    public static final String WRONG_ACCESS = "잘못된 접근입니다";

    public DomainStateException(String s) {
        super(s);
    }
}
