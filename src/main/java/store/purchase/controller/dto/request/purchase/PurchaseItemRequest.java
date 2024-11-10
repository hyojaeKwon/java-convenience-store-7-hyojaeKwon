package store.purchase.controller.dto.request.purchase;

public class PurchaseItemRequest {
    private final String name;
    private final long quantity;

    public PurchaseItemRequest(String name, long quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public long getQuantity() {
        return quantity;
    }
}
