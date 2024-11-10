package store.purchase.service.repository;

import store.purchase.domain.Purchase;

public interface PurchaseRepository {
    void save(Purchase purchase);

    Purchase findById(String id);
}
