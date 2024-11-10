package store.purchase.controller.dto.request.purchase;

import java.util.List;

public class PurchaseRequest {
    private final List<PurchaseItemRequest> purchaseItems;

    public PurchaseRequest(List<PurchaseItemRequest> purchaseItems) {
        this.purchaseItems = purchaseItems;
    }

    public List<PurchaseItemRequest> getPurchaseItems() {
        return purchaseItems;
    }
}
