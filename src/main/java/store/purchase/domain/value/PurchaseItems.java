package store.purchase.domain.value;

import java.util.List;

public class PurchaseItems {

    private final List<PurchaseItem> purchaseList;

    public PurchaseItems(List<PurchaseItem> purchaseList) {
        this.purchaseList = purchaseList;
    }

    public List<PurchaseItem> getPurchaseList() {
        return purchaseList;
    }

    public long sumOfPurchaseItems() {
        return purchaseList.stream().map(i -> i.getPrice() * i.getAmount()).reduce(0L, Long::sum);
    }
}
