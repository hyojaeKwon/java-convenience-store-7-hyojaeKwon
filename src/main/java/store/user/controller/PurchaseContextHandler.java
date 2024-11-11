package store.user.controller;

import java.util.ArrayList;
import java.util.List;
import store.common.util.IdHolder;
import store.purchase.controller.PurchaseController;
import store.purchase.controller.dto.request.promotion.ResolveItemRequest;
import store.purchase.controller.dto.request.promotion.ResolveRequest;
import store.purchase.controller.dto.request.purchase.PurchaseRequest;
import store.purchase.controller.dto.response.PromotionConflict;
import store.purchase.controller.dto.response.PromotionConflictResponse;
import store.purchase.domain.discount.promotion.PromotionDiscountStatus;
import store.purchase.domain.value.receipt.Receipt;
import store.user.controller.formatter.PromotionOutputFormatter;
import store.user.controller.formatter.ReceiptOutputFormatter;
import store.user.controller.handler.ConsoleInputHandler;
import store.user.controller.handler.ConsoleOutputHandler;
import store.user.controller.handler.retry.RetryHandler;

public class PurchaseContextHandler {

    private final ConsoleInputHandler inputHandler;
    private final PurchaseController purchaseController;
    private final PromotionOutputFormatter promotionFormatter;
    private final ConsoleOutputHandler outputHandler;
    private final ReceiptOutputFormatter receiptFormatter;
    private final IdHolder idHolder;

    public PurchaseContextHandler(ConsoleInputHandler inputHandler, PurchaseController purchaseController,
                                  PromotionOutputFormatter promotionFormatter, ConsoleOutputHandler outputHandler,
                                  ReceiptOutputFormatter receiptFormatter, IdHolder idHolder) {
        this.inputHandler = inputHandler;
        this.purchaseController = purchaseController;
        this.promotionFormatter = promotionFormatter;
        this.outputHandler = outputHandler;
        this.receiptFormatter = receiptFormatter;
        this.idHolder = idHolder;
    }

    public void run() {
        String user = idHolder.generateId();
        createPurchase(user);
        terminalPurchase(user);
    }

    public void createPurchase(String user) {
        PromotionConflictResponse response = purchaseRequest(user);
        List<ResolveRequest> requests = resolveResponse(response);
        purchaseController.resolveConflict(user, new ResolveItemRequest(requests));
    }

    public void terminalPurchase(String user) {
        requestMembership(user);
        requestReceipt(user);
    }

    private PromotionConflictResponse purchaseRequest(String user) {
        outputHandler.printPurchaseMessage();
        return RetryHandler.retry(() -> {
            PurchaseRequest purchaseRequest = inputHandler.purchaseInput();
            return purchaseController.createPurchase(user, purchaseRequest);
        });
    }

    private List<ResolveRequest> resolveResponse(PromotionConflictResponse response) {
        List<ResolveRequest> requests = new ArrayList<>();
        for (PromotionConflict conflict : response.getConflictList()) {
            if (conflict.getStatus() == PromotionDiscountStatus.RESOLVED) {
                continue;
            }
            String conflictString = promotionFormatter.format(conflict);
            controlConflict(conflict, conflictString, requests);
        }
        return requests;
    }

    private void controlConflict(PromotionConflict conflict, String conflictString, List<ResolveRequest> requests) {
        RetryHandler.retry(() -> {
            outputHandler.printContextMessage(conflictString);
            boolean control = inputHandler.controlInput();
            requests.add(ResolveRequest.of(conflict, control));
            return null;
        });
    }

    private void requestMembership(String user) {
        outputHandler.printMemberShip();
        RetryHandler.retry(() -> {
            boolean flag = inputHandler.controlInput();
            purchaseController.requestMembership(user, flag);
            return null;
        });
    }

    private void requestReceipt(String user) {
        Receipt receipt = purchaseController.getReceipt(user);
        String formattedReceipt = receiptFormatter.format(receipt);
        outputHandler.printContextMessage(formattedReceipt);
    }
}
