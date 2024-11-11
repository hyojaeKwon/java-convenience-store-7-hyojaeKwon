package store.item.domain.item;

import static store.common.exception.domain.DomainArgumentException.RULE_EXPIRATION_INVALID;
import static store.common.exception.domain.DomainArgumentException.RULE_NAME;
import static store.common.exception.domain.DomainArgumentException.RULE_QUANTITY;

import java.time.LocalDateTime;
import store.common.exception.domain.DomainArgumentException;
import store.item.controller.dto.request.PromotionRuleRequest;

public class PromotionRule {

    private final String name;
    private final long buyQuantity;
    private final long promotionQuantity;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;

    private PromotionRule(String name, long buyQuantity, long promotionQuantity, LocalDateTime startDate,
                          LocalDateTime endDate) {
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
        validateName(promotionRuleRequest);
        validateQuantity(promotionRuleRequest);
        validateDateRange(promotionRuleRequest);
    }

    private static void validateName(PromotionRuleRequest promotionRuleRequest) {
        if (promotionRuleRequest.getName() == null || promotionRuleRequest.getName().isEmpty()) {
            throw new DomainArgumentException(RULE_NAME);
        }
    }

    private static void validateQuantity(PromotionRuleRequest promotionRuleRequest) {
        if (promotionRuleRequest.getBuyQuantity() <= 0 || promotionRuleRequest.getGetQuantity() <= 0) {
            throw new DomainArgumentException(RULE_QUANTITY);
        }
    }

    private static void validateDateRange(PromotionRuleRequest promotionRuleRequest) {
        if (!promotionRuleRequest.getEndDate().isAfter(promotionRuleRequest.getStartDate())) {
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

    public boolean isPromotionDate(LocalDateTime now) {
        return startDate.isBefore(now) && endDate.isAfter(now);
    }

    public long getPromotionQuantitySum() {
        return buyQuantity + promotionQuantity;
    }

    public String getName() {
        return name;
    }
}
