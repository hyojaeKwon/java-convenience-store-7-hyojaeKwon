package store.item.controller;

import store.item.domain.item.Item;
import store.item.domain.item.PromotionItem;

public interface ItemStockService {
    void updateItemStock(Item item, PromotionItem promotionItem);
}
