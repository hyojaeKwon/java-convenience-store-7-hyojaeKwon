package store.purchase.service;

import java.util.List;
import store.item.controller.ItemPurchaseService;
import store.purchase.controller.PurchaseDiscountService;
import store.purchase.controller.PurchaseService;
import store.purchase.controller.dto.GeneralTransfer;
import store.purchase.controller.dto.request.promotion.ResolveItemRequest;
import store.purchase.controller.dto.request.promotion.ResolveRequest;
import store.purchase.controller.dto.request.purchase.PurchaseRequest;
import store.purchase.controller.dto.response.PromotionConflictResponse;
import store.purchase.domain.Purchase;
import store.purchase.domain.PurchaseCreate;
import store.purchase.domain.discount.promotion.PromotionDiscountStatus;
import store.purchase.domain.value.receipt.Receipt;
import store.purchase.service.factory.PurchaseCreationFactory;
import store.purchase.service.factory.promotion.PromotionDiscountConflictFactory;
import store.purchase.service.repository.PurchaseRepository;

public class PurchaseServiceImpl implements PurchaseService {

    private final PromotionDiscountConflictFactory conflictFactory;
    private final PurchaseDiscountService purchaseDiscountService;
    private final PurchaseCreationFactory purchaseCreationFactory;
    private final PurchaseRepository purchaseRepository;
    private final ItemPurchaseService itemPurchaseService;

    public PurchaseServiceImpl(PromotionDiscountConflictFactory conflictFactory,
                               PurchaseDiscountService purchaseDiscountService,
                               PurchaseCreationFactory purchaseCreationFactory, PurchaseRepository purchaseRepository,
                               ItemPurchaseService itemPurchaseService) {
        this.conflictFactory = conflictFactory;
        this.purchaseDiscountService = purchaseDiscountService;
        this.purchaseCreationFactory = purchaseCreationFactory;
        this.purchaseRepository = purchaseRepository;
        this.itemPurchaseService = itemPurchaseService;
    }

    @Override
    public PromotionConflictResponse createPurchase(GeneralTransfer<PurchaseRequest> request) {
        List<PurchaseCreate> purchaseCreates = purchaseCreationFactory.handlePurchaseRequest(request.getContext());
        Purchase purchase = Purchase.createPurchase(purchaseCreates, request.getUserId());
        purchase = purchaseDiscountService.applyPromotionDiscount(purchase);
        purchaseRepository.save(purchase);
        return conflictFactory.getPromotionConflicts(purchase);
    }

    @Override
    public void solvePromotionConflict(GeneralTransfer<ResolveItemRequest> request) {
        Purchase purchase = purchaseRepository.findById(request.getUserId());
        List<ResolveRequest> conflicts = request.getContext().getConflicts();

        for (ResolveRequest resolveRequest : conflicts) {
            purchase = resovlePurchase(purchase, resolveRequest);
        }
        purchase = purchase.resolveAllPromotionConflicts();
        purchaseRepository.save(purchase);
    }

    private Purchase resovlePurchase(Purchase purchase, ResolveRequest request) {
        if (request.getStatus() == PromotionDiscountStatus.GET_MORE && (request.isSolve())) {
            return purchase.addPurchaseAmount(request.getName(), request.getQuantity());
        }
        if (request.getStatus() == PromotionDiscountStatus.CANT_DISCOUNT_ALL && (!request.isSolve())) {
            return purchase.removePurchaseAmount(request.getName(), request.getQuantity());
        }
        return purchase;
    }

    @Override
    public void applyMemberShip(GeneralTransfer<Boolean> request) {
        Purchase purchase = purchaseRepository.findById(request.getUserId());
        if (Boolean.TRUE.equals(request.getContext())) {
            purchase = purchaseDiscountService.applyMembershipDiscount(purchase);
        }
        purchaseRepository.save(purchase);
    }

    @Override
    public Receipt getReceipt(GeneralTransfer<Void> request) {
        Purchase purchase = purchaseRepository.findById(request.getUserId());
        purchase.getPurchaseItems().forEach(purchaseItem -> itemPurchaseService.purchase(purchaseItem.getName(), purchaseItem.getAmount()));
        return Receipt.create(purchase);
    }

}
