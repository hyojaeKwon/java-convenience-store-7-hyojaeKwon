package store.purchase.infrastructure;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import store.purchase.domain.Purchase;
import store.purchase.service.repository.PurchaseRepository;

public class PurchaseRepositoryImpl implements PurchaseRepository {


    private static PurchaseRepository instance;
    private final Map<String, Purchase> purchaseMap = new ConcurrentHashMap<>();

    private PurchaseRepositoryImpl() {
    }

    public static PurchaseRepository getInstance() {
        if (instance == null) {
            checkSyncInstance();
        }
        return instance;
    }

    private static void checkSyncInstance() {
        synchronized (PurchaseRepositoryImpl.class) {
            if (instance == null) {
                instance = new PurchaseRepositoryImpl();
            }
        }
    }

    @Override
    public void save(Purchase purchase) {
        if (purchase == null) {
            throw new IllegalArgumentException("Purchase must not be null");
        }
        purchaseMap.put(purchase.getId(), purchase);
    }

    @Override
    public Purchase findById(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Purchase id must not be null");
        }
        return Optional.ofNullable(purchaseMap.get(id))
                .orElseThrow(() -> new IllegalArgumentException("Purchase not found"));
    }
}
