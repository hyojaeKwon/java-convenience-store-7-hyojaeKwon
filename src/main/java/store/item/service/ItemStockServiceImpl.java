package store.item.service;

import store.item.controller.ItemStockService;
import store.item.domain.item.Item;
import store.item.domain.item.PromotionItem;
import store.item.service.repository.ItemRepository;
import store.item.service.repository.PromotionItemRepository;

public class ItemStockServiceImpl implements ItemStockService {

    private final ItemRepository itemRepository;
    private final PromotionItemRepository promotionItemRepository;

    public ItemStockServiceImpl(ItemRepository itemRepository, PromotionItemRepository promotionItemRepository) {
        this.itemRepository = itemRepository;
        this.promotionItemRepository = promotionItemRepository;
    }

    @Override
    public void updateItemStock(Item item, PromotionItem promotionItem) {
        if (item.isPromotion()) {
            item = updatePromotionItem(item, promotionItem);
        }
        updateGeneralItem(item);
    }

    private Item updatePromotionItem(Item item, PromotionItem promotionItem) {
        if (promotionItem.getPromotionStockQuantity() == 0) {
            promotionItemRepository.delete(promotionItem.getItemId());
            item = item.closePromotion();
            return item;
        }
        promotionItemRepository.update(promotionItem.getPromotionItemId(), promotionItem);
        return item;
    }

    private void updateGeneralItem(Item item) {
        if (item.getStockQuantity() == 0) {
            itemRepository.delete(item.getId());
            return;
        }
        itemRepository.update(item.getId(), item);
    }
}
