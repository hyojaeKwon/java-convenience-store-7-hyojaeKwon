package store.purchase.service.factory;

import static store.common.exception.service.ServiceArgumentException.EXCEED_AMOUNT;

import java.util.List;
import store.common.exception.service.ServiceArgumentException;
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
            throw new ServiceArgumentException(EXCEED_AMOUNT);
        }
        return PurchaseCreate.create(request, itemInfo.getPrice(), itemInfo.isPromotion());
    }
}
