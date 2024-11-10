package store.purchase.domain;

import java.util.List;
import store.purchase.domain.discount.membership.MembershipDiscount;
import store.purchase.domain.discount.promotion.PromotionDiscount;
import store.purchase.domain.value.PurchaseItem;
import store.purchase.domain.value.PurchaseItems;

public class Purchase {
    private final PurchaseItems purchaseItems;
    private final PromotionDiscount promotionDiscount;
    private final MembershipDiscount membershipDiscount;
    private final PurchaseStatus status;
    private final String id;

    public Purchase(PurchaseItems purchaseItems, PromotionDiscount promotionDiscount,
                    MembershipDiscount membershipDiscount, PurchaseStatus status, String id) {
        this.purchaseItems = purchaseItems;
        this.promotionDiscount = promotionDiscount;
        this.membershipDiscount = membershipDiscount;
        this.status = status;
        this.id = id;
    }

    public static Purchase createPurchase(List<PurchaseCreate> purchaseCreates, String id) {
        List<PurchaseItem> purchaseList = purchaseCreates.stream().map(PurchaseItem::create).toList();
        return new Purchase(new PurchaseItems(purchaseList), null, null, PurchaseStatus.CREATED, id);
    }

    public Purchase applyPromotionDiscount(PromotionDiscount promotionDiscountInput) {
        return new Purchase(purchaseItems, promotionDiscountInput, membershipDiscount, PurchaseStatus.CONFLICTED, id);
    }

    public Purchase applyMembershipDiscount(MembershipDiscount membershipDiscountInput) {
        return new Purchase(purchaseItems, promotionDiscount, membershipDiscountInput, status, id);
    }

    public Purchase resolveAllPromotionConflicts() {
        return new Purchase(purchaseItems, promotionDiscount, membershipDiscount, PurchaseStatus.RESOLVED, id);
    }

    public Purchase addPurchaseAmount(String name, long quantity) {
        return updatePurchaseAmount(name, quantity); // 수량을 더함
    }

    public Purchase removePurchaseAmount(String name, long quantity) {
        return updatePurchaseAmount(name, -quantity); // 수량을 뺌
    }

    private Purchase updatePurchaseAmount(String name, long quantity) {
        List<PurchaseItem> purchaseItemList = updatePurchaseItem(name, quantity);

        if (quantity > 0) {
            PromotionDiscount resolveDiscount = promotionDiscount.resolveGetMoreConflict(name);
            return new Purchase(new PurchaseItems(purchaseItemList), resolveDiscount, membershipDiscount, status, id);
        }
        return new Purchase(new PurchaseItems(purchaseItemList), promotionDiscount, membershipDiscount, status, id);
    }

    private List<PurchaseItem> updatePurchaseItem(String name, long quantity) {
        return purchaseItems.getPurchaseList().stream().filter(item -> item.getName().equals(name))
                .map(purchaseItem -> purchaseItem.addPurchaseAmount(quantity)).toList();
    }

    public List<PurchaseItem> getPurchaseItems() {
        return purchaseItems.getPurchaseList();
    }

    public long getPurchaseSum() {
        return purchaseItems.sumOfPurchaseItems();
    }

    public PromotionDiscount getPromotionDiscount() {
        if (promotionDiscount == null) {
            throw new IllegalStateException();
        }
        return promotionDiscount;
    }

    public MembershipDiscount getMembershipDiscount() {
        if (status != PurchaseStatus.RESOLVED) {
            throw new IllegalStateException();
        }
        return membershipDiscount;
    }

    public String getId() {
        return id;
    }
}
