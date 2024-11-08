package store.item.service;

import java.util.ArrayList;
import java.util.List;
import store.common.util.IdHolder;
import store.item.controller.ItemSaveService;
import store.item.controller.dto.request.ItemRequest;
import store.item.controller.dto.request.ItemSaveRequest;
import store.item.domain.item.Item;
import store.item.domain.item.PromotionItem;
import store.item.service.repository.ItemRepository;
import store.item.service.repository.PromotionItemRepository;

public class ItemSaveServiceImpl implements ItemSaveService {

    private final ItemRepository itemRepository;
    private final PromotionItemRepository promotionItemRepository;
    private final IdHolder idHolder;

    public ItemSaveServiceImpl(ItemRepository itemRepository, PromotionItemRepository promotionItemRepository,
                               IdHolder idHolder) {
        this.itemRepository = itemRepository;
        this.promotionItemRepository = promotionItemRepository;
        this.idHolder = idHolder;
    }

    @Override
    public void save(ItemSaveRequest itemSaveRequest) {

        List<Item> itemList = new ArrayList<>();
        List<PromotionItem> promotionItemList = new ArrayList<>();

        separateItemRequests(itemSaveRequest, promotionItemList, itemList);
        itemList.forEach(this::saveGeneralItem);
        promotionItemList.forEach(this::savePromotionItem);
    }

    private void separateItemRequests(ItemSaveRequest itemSaveRequest, List<PromotionItem> promotionItems, List<Item> itemList) {
        itemSaveRequest.getItems().forEach(itemRequest -> {
            if (!isPromotionItem(itemRequest)) {
                itemList.add(Item.create(idHolder, itemRequest));
            }
            if (isPromotionItem(itemRequest)) {
                findPromotionItemNameAndAddList(promotionItems, itemList, itemRequest);
            }
        });
    }

    private void findPromotionItemNameAndAddList(List<PromotionItem> promotionItems, List<Item> itemList, ItemRequest itemRequest) {
        itemList.forEach(item -> {
            if(item.getName().equals(itemRequest.getName())){
                promotionItems.add(PromotionItem.create(idHolder, item, itemRequest));
            }
        });
    }

    private void saveGeneralItem(Item item) {
        itemRepository.save(item.getId(), item);
    }

    private void savePromotionItem(PromotionItem promotionItem) {
        promotionItemRepository.save(promotionItem.getPromotionItemId(), promotionItem);
    }

    private boolean isPromotionItem(ItemRequest itemRequest) {
        return itemRequest.isPromotionRuleExist();
    }
}
