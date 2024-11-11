package store.item.service;

import static store.common.exception.service.ServiceArgumentException.ITEM_NOT_FOUND;

import java.util.Optional;
import store.common.exception.service.ServiceArgumentException;
import store.item.controller.ItemPurchaseService;
import store.item.controller.ItemStockService;
import store.item.domain.item.Item;
import store.item.domain.item.PromotionItem;
import store.item.domain.item.value.ItemInfo;
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
                .orElseThrow(() -> new ServiceArgumentException(ITEM_NOT_FOUND));
        Optional<PromotionItem> promotionItem = promotionItemRepository.findByName(itemName);
        ItemInfo itemInfo = promotionItem.map(value -> ItemInfo.createPromotionItemInfo(item, value))
                .orElseGet(() -> ItemInfo.createNotPromotionItemInfo(item));

        ItemInfo purchased = itemInfo.purchase(quantity);
        itemStockService.updateItemStock(purchased);
    }

}
