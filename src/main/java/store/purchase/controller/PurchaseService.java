package store.purchase.controller;

import store.purchase.controller.dto.GeneralTransfer;
import store.purchase.controller.dto.request.promotion.ResolveItemRequest;
import store.purchase.controller.dto.request.purchase.PurchaseRequest;
import store.purchase.controller.dto.response.PromotionConflictResponse;
import store.purchase.domain.value.receipt.Receipt;

public interface PurchaseService {
    PromotionConflictResponse createPurchase(GeneralTransfer<PurchaseRequest> request);

    void solvePromotionConflict(GeneralTransfer<ResolveItemRequest> request);

    void applyMemberShip(GeneralTransfer<Boolean> request);

    Receipt getReceipt(GeneralTransfer<Void> request);
}
