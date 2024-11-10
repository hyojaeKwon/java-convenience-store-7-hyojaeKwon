package store.user.controller.formatter;

import store.purchase.controller.dto.response.PromotionConflict;
import store.purchase.domain.discount.promotion.PromotionDiscountStatus;

public class PromotionOutputFormatter {

    private static final String CANNOT_DISCOUNT_FORMAT = "현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)";
    private static final String GET_MORE_FORMAT = "현재 %s은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)";

    public String format(PromotionConflict conflict) {
        if (conflict.getStatus() == PromotionDiscountStatus.GET_MORE) {
            return formatGetMore(conflict.getName());
        }
        if (conflict.getStatus() == PromotionDiscountStatus.CANT_DISCOUNT_ALL) {
            return formatCannotDiscount(conflict.getName(), conflict.getQuantity());
        }
        throw new IllegalArgumentException("지원되지 않는 형식입니다");
    }

    public String formatGetMore(String name) {
        return String.format(GET_MORE_FORMAT, name);
    }

    public String formatCannotDiscount(String name, long quantity) {
        return String.format(CANNOT_DISCOUNT_FORMAT, name, quantity);
    }
}
