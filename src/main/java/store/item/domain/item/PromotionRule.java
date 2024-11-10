package store.item.domain.item;

import static store.common.exception.domain.DomainArgumentException.RULE_EXPIRATION_INVALID;
import static store.common.exception.domain.DomainArgumentException.RULE_NAME;
import static store.common.exception.domain.DomainArgumentException.RULE_QUANTITY;

import java.util.Date;
import store.common.exception.domain.DomainArgumentException;
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
        if (promotionRuleRequest.getName() == null || promotionRuleRequest.getName().isEmpty()) {
            throw new DomainArgumentException(RULE_NAME);
        }
        if (promotionRuleRequest.getBuyQuantity() <= 0 || promotionRuleRequest.getGetQuantity() <= 0) {
            throw new DomainArgumentException(RULE_QUANTITY);
        }
        if (!promotionRuleRequest.getEndDate().after(promotionRuleRequest.getStartDate())) {
            throw new DomainArgumentException(RULE_EXPIRATION_INVALID);
        }
    }

    public long getPromotionQuantity(long buyQuantityInput) {
        long promotionCount = buyQuantityInput / (buyQuantity + promotionQuantity);
        return promotionQuantity * promotionCount;
    }

    public boolean canGetOneMore(long buyQuantityInput) {
        return (buyQuantity) == (buyQuantityInput % (buyQuantity + promotionQuantity));
    }

    public boolean isPromotionDate(Date nowDate) {
        return startDate.after(nowDate) && endDate.before(nowDate);
    }

    public long getPromotionQuantitySum() {
        return buyQuantity + promotionQuantity;
    }

    public String getName() {
        return name;
    }
}
