package store.purchase.service.factory;

import java.util.List;
import store.item.controller.ItemStockService;
import store.item.domain.item.value.ItemInfo;
import store.purchase.controller.dto.request.purchase.PurchaseItemRequest;
import store.purchase.controller.dto.request.purchase.PurchaseRequest;
import store.purchase.domain.PurchaseCreate;

public class PurchaseCreationFactory {

    private final ItemStockService itemStockService;

    public PurchaseCreationFactory(ItemStockService itemStockService) {
        this.itemStockService = itemStockService;
    }

    public List<PurchaseCreate> handlePurchaseRequest(PurchaseRequest request) {
        return request.getPurchaseItems().stream().map(this::createPurchase).toList();
    }

    private PurchaseCreate createPurchase(PurchaseItemRequest request) {
        ItemInfo itemInfo = itemStockService.getItemInfoByName(request.getName());
        if (!itemInfo.canPurchase(request.getQuantity())) {
            throw new IllegalArgumentException("구매할 수 없다");
        }
        return PurchaseCreate.create(request, itemInfo.getPrice(), itemInfo.isPromotion());
    }
}
