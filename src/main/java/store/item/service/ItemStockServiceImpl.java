package store.item.service;

import static store.common.exception.service.ServiceArgumentException.ITEM_NOT_FOUND;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import store.common.exception.service.ServiceArgumentException;
import store.item.controller.ItemStockService;
import store.item.domain.item.Item;
import store.item.domain.item.PromotionItem;
import store.item.domain.item.value.ItemInfo;
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
    public void updateItemStock(ItemInfo itemInfo) {
        if (itemInfo.isPromotion()) {
            updatePromotionItem(itemInfo.getPromotionItem());
        }
        updateGeneralItem(itemInfo.getItem());
    }

    @Override
    public List<ItemInfo> getAllItemInfo() {
        List<Item> items = itemRepository.findAll();
        List<ItemInfo> itemInfos = new ArrayList<>();

        for (Item item : items) {
            Optional<PromotionItem> promotionItem = promotionItemRepository.findByName(item.getName());
            promotionItem.map(value -> itemInfos.add(ItemInfo.createPromotionItemInfo(item, value)))
                    .orElseGet(() -> itemInfos.add(ItemInfo.createNotPromotionItemInfo(item)));
        }
        return itemInfos;
    }

    @Override
    public ItemInfo getItemInfoByName(String name) {
        Item item = itemRepository.findByName(name).orElseThrow(() -> new ServiceArgumentException(ITEM_NOT_FOUND));
        Optional<PromotionItem> promotionItem = promotionItemRepository.findByName(name);
        return promotionItem.map(value -> ItemInfo.createPromotionItemInfo(item, value))
                .orElseGet(() -> ItemInfo.createNotPromotionItemInfo(item));
    }

    private void updatePromotionItem(PromotionItem promotionItem) {
        promotionItemRepository.update(promotionItem.getPromotionItemId(), promotionItem);
    }

    private void updateGeneralItem(Item item) {
        itemRepository.update(item.getId(), item);
    }
}
