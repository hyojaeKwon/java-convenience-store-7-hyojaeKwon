package store.purchase.controller.dto.response;

import java.util.List;

public class PromotionConflictResponse {
    private final List<PromotionConflict> conflictList;

    public PromotionConflictResponse(List<PromotionConflict> conflictList) {
        this.conflictList = conflictList;
    }

    public List<PromotionConflict> getConflictList() {
        return conflictList;
    }
}
