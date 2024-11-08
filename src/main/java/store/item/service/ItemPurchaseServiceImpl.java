package store.item.service;

import store.item.controller.ItemPurchaseService;
import store.item.controller.ItemStockService;
import store.item.domain.item.Item;
import store.item.domain.item.PromotionItem;
import store.item.service.repository.ItemRepository;
import store.item.service.repository.PromotionItemRepository;

public class ItemPurchaseServiceImpl implements ItemPurchaseService {

    private final ItemRepository itemRepository;
    private final PromotionItemRepository promotionItemRepository;
    private final ItemStockService itemStockService;

    public ItemPurchaseServiceImpl(ItemRepository itemRepository, PromotionItemRepository promotionItemRepository,
                                   ItemStockService itemStockService) {
        this.itemRepository = itemRepository;
        this.promotionItemRepository = promotionItemRepository;
        this.itemStockService = itemStockService;
    }

    @Override
    public void purchase(String itemName, long quantity) {
        Item item = itemRepository.findByName(itemName)
                .orElseThrow(() -> new IllegalArgumentException("item not found"));
        purchaseByItemPromotion(itemName, quantity, item);
    }

    private void purchaseByItemPromotion(String itemName, long quantity, Item item) {
        if (item.isPromotion()) {
            purchasePromotionItem(itemName, quantity, item);
            return;
        }
        purchaseGeneralItem(item, quantity);
    }

    private void purchasePromotionItem(String itemName, long quantity, Item item) {
        PromotionItem promotionItem = promotionItemRepository.findByName(itemName)
                .orElseThrow(() -> new IllegalArgumentException("Promotion item not found"));
        long promotionStockQuantity = promotionItem.getPromotionStockQuantity();

        Result updatedItemInfo = updatePurchaseItemQuantity(quantity, item, promotionStockQuantity, promotionItem);
        itemStockService.updateItemStock(updatedItemInfo.item(), updatedItemInfo.promotionItem());
    }

    private Result updatePurchaseItemQuantity(long quantity, Item item, long promotionStockQuantity,
                                              PromotionItem promotionItem) {
        if (quantity > promotionStockQuantity) {
            promotionItem = promotionItem.purchase(promotionStockQuantity);
            item = item.purchase(quantity - promotionStockQuantity);
            return new Result(item, promotionItem);
        }
        promotionItem = promotionItem.purchase(quantity);
        return new Result(item, promotionItem);
    }

    private void purchaseGeneralItem(Item item, long quantity) {
        if (item.getStockQuantity() < quantity) {
            throw new IllegalArgumentException();
        }
        item = item.purchase(quantity);
        itemStockService.updateItemStock(item, null);
    }

    private record Result(Item item, PromotionItem promotionItem) {
    }
}
