package store.common.exception.service;

public class ServiceArgumentException extends IllegalArgumentException {
    public static final String ITEM_NOT_FOUND = "해당하는 항목이 존재하지 않습니다.";
    public static final String RULE_NOT_FOUND = "해당하는 프로모션이 존재하지 않습니다.";
    public static final String EXCEED_AMOUNT = "재고 수량을 초과하여 구매할 수 없습니다.";

    public ServiceArgumentException(String s) {
        super(s);
    }
}
