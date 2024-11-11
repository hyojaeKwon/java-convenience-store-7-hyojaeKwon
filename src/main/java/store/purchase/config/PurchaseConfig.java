package store.purchase.config;

import store.item.config.ItemConfig;
import store.purchase.controller.PurchaseController;
import store.purchase.controller.PurchaseDiscountService;
import store.purchase.controller.PurchaseService;
import store.purchase.infrastructure.PurchaseRepositoryImpl;
import store.purchase.service.PurchaseDiscountServiceImpl;
import store.purchase.service.PurchaseServiceImpl;
import store.purchase.service.factory.PurchaseCreationFactory;
import store.purchase.service.factory.promotion.PromotionDiscountConflictFactory;
import store.purchase.service.factory.promotion.PromotionDiscountInfoFactory;
import store.purchase.service.repository.PurchaseRepository;

public class PurchaseConfig {

    private final ItemConfig itemConfig;

    public PurchaseConfig(ItemConfig itemConfig) {
        this.itemConfig = itemConfig;
    }

    public PurchaseController purchaseController() {
        return new PurchaseController(purchaseService());
    }

    private PurchaseService purchaseService() {
        return new PurchaseServiceImpl(promotionDiscountConflictFactory(), purchaseDiscountService(),
                purchaseCreationFactory(), purchaseRepository(), itemConfig.itemPurchaseService());
    }

    private PurchaseDiscountService purchaseDiscountService() {
        return new PurchaseDiscountServiceImpl(promotionDiscountInfoFactory());
    }

    private PromotionDiscountInfoFactory promotionDiscountInfoFactory() {
        return new PromotionDiscountInfoFactory(itemConfig.itemStockService());
    }

    private PromotionDiscountConflictFactory promotionDiscountConflictFactory() {
        return new PromotionDiscountConflictFactory();
    }

    private PurchaseCreationFactory purchaseCreationFactory() {
        return new PurchaseCreationFactory(itemConfig.itemStockService());
    }

    private PurchaseRepository purchaseRepository() {
        return PurchaseRepositoryImpl.getInstance();
    }
}
