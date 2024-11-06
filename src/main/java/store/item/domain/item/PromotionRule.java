package store.item.domain.item;

import java.util.Date;

public class PromotionRule {

    private final String name;
    private final int buyQuantity;
    private final int promotionQuantity;
    private final Date startDate;
    private final Date endDate;

    public PromotionRule(String name, int buyQuantity, int promotionQuantity, Date startDate, Date endDate) {
        this.name = name;
        this.buyQuantity = buyQuantity;
        this.promotionQuantity = promotionQuantity;
        this.startDate = startDate;
        this.endDate = endDate;
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
