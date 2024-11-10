package store.purchase.controller;

import store.purchase.controller.dto.GeneralTransfer;
import store.purchase.controller.dto.request.promotion.ResolveItemRequest;
import store.purchase.controller.dto.request.purchase.PurchaseRequest;
import store.purchase.controller.dto.response.PromotionConflictResponse;
import store.purchase.domain.value.receipt.Receipt;

public class PurchaseController {

    private final PurchaseService purchaseService;

    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    public PromotionConflictResponse createPurchase(String id, PurchaseRequest request) {
        return purchaseService.createPurchase(new GeneralTransfer<>(id, request));
    }

    public void resolveConflict(String id, ResolveItemRequest request) {
        purchaseService.solvePromotionConflict(new GeneralTransfer<>(id, request));
    }

    public void requestMembership(String id, boolean control) {
        purchaseService.applyMemberShip(new GeneralTransfer<>(id, control));
    }

    public Receipt getReceipt(String id) {
        return purchaseService.getReceipt(new GeneralTransfer<>(id, null));
    }

}
