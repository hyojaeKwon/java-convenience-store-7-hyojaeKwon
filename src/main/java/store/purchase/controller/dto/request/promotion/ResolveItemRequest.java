package store.purchase.controller.dto.request.promotion;

import java.util.List;

public class ResolveItemRequest {

    private final List<ResolveRequest> conflicts;

    public ResolveItemRequest(List<ResolveRequest> conflicts) {
        this.conflicts = conflicts;
    }

    public List<ResolveRequest> getConflicts() {
        return conflicts;
    }
}
