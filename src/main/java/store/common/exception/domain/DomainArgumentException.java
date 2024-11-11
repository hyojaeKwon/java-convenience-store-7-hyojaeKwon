package store.common.exception.domain;

public class DomainArgumentException extends IllegalArgumentException {
    public static final String EXCEED_AMOUNT = "재고 수량을 초과하여 구매할 수 없습니다.";
    public static final String NEG_AMOUNT = "구매 수량은 0이나 음수일 수 없습니다.";
    public static final String RULE_QUANTITY = "프로모션 규칙은 0이나 음수일 수 없습니다.";
    public static final String RULE_EXPIRATION_INVALID = "프로모션 기한이 유효하지 않습니다.";
    public static final String RULE_NAME = "프로모션 이름은 null이나 공백일 수 없습니다";

    public static final String PURCHASE_NEG_AMOUNT = "구매 수량은 0이나 음수일 수 없습니다.";

    public DomainArgumentException(String s) {
        super(s);
    }
}
