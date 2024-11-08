package store.item.domain.item;

import java.util.Date;
import store.item.controller.dto.request.PromotionRuleRequest;

public class PromotionRule {

    private final String name;
    private final long buyQuantity;
    private final long promotionQuantity;
    private final Date startDate;
    private final Date endDate;

    private PromotionRule(String name, long buyQuantity, long promotionQuantity, Date startDate, Date endDate) {
        this.name = name;
        this.buyQuantity = buyQuantity;
        this.promotionQuantity = promotionQuantity;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static PromotionRule create(PromotionRuleRequest promotionRuleRequest) {
        validate(promotionRuleRequest);
        return new PromotionRule(promotionRuleRequest.getName(), promotionRuleRequest.getBuyQuantity(),
                promotionRuleRequest.getGetQuantity(), promotionRuleRequest.getStartDate(),
                promotionRuleRequest.getEndDate());
    }

    private static void validate(PromotionRuleRequest promotionRuleRequest) {
        if (promotionRuleRequest.getBuyQuantity() <= 0 || promotionRuleRequest.getGetQuantity() <= 0) {
            throw new IllegalArgumentException();
        }
        if (promotionRuleRequest.getEndDate().after(promotionRuleRequest.getStartDate())) {
            throw new IllegalArgumentException();
        }
    }

    public long getPromotionQuantity(long buyQuantityInput) {
        long promotionCount = buyQuantityInput / buyQuantity;
        return promotionQuantity * promotionCount;
    }

    public boolean isPromotionDate(Date nowDate) {
        return startDate.after(nowDate) && endDate.before(nowDate);
    }

    public String getName() {
        return name;
    }
}
