package store.item.controller.dto.request;

import java.time.LocalDateTime;

public class PromotionRuleRequest {

    private final String name;
    private final long buyQuantity;
    private final long getQuantity;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;

    public PromotionRuleRequest(String name, long buyQuantity, long getQuantity, LocalDateTime startDate,
                                LocalDateTime endDate) {
        this.name = name;
        this.buyQuantity = buyQuantity;
        this.getQuantity = getQuantity;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getName() {
        return name;
    }

    public long getBuyQuantity() {
        return buyQuantity;
    }

    public long getGetQuantity() {
        return getQuantity;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }
}
