package store.purchase.controller;

import store.purchase.domain.Purchase;

public interface PurchaseDiscountService {

    Purchase applyMembershipDiscount(Purchase purchase);

    Purchase applyPromotionDiscount(Purchase purchase);
}
