package store.item.service;

import static store.common.exception.service.ServiceArgumentException.ITEM_NOT_FOUND;
import static store.common.exception.service.ServiceArgumentException.RULE_NOT_FOUND;

import camp.nextstep.edu.missionutils.DateTimes;
import java.util.ArrayList;
import java.util.List;
import store.common.exception.service.ServiceArgumentException;
import store.common.util.IdHolder;
import store.item.controller.ItemSaveService;
import store.item.controller.dto.request.ItemRequest;
import store.item.controller.dto.request.ItemSaveRequest;
import store.item.controller.dto.request.PromotionRuleRequest;
import store.item.domain.item.Item;
import store.item.domain.item.PromotionItem;
import store.item.domain.item.PromotionRule;
import store.item.service.repository.ItemRepository;
import store.item.service.repository.PromotionItemRepository;
import store.item.service.repository.PromotionRuleRepository;

public class ItemSaveServiceImpl implements ItemSaveService {

    private final ItemRepository itemRepository;
    private final PromotionItemRepository promotionItemRepository;
    private final PromotionRuleRepository promotionRuleRepository;
    private final IdHolder idHolder;

    public ItemSaveServiceImpl(ItemRepository itemRepository, PromotionItemRepository promotionItemRepository,
                               PromotionRuleRepository promotionRuleRepository, IdHolder idHolder) {
        this.itemRepository = itemRepository;
        this.promotionItemRepository = promotionItemRepository;
        this.promotionRuleRepository = promotionRuleRepository;
        this.idHolder = idHolder;
    }

    @Override
    public void saveRule(List<PromotionRuleRequest> request) {
        request.forEach(promotionRuleRequest -> {
            PromotionRule promotionRule = PromotionRule.create(promotionRuleRequest);
            promotionRuleRepository.save(promotionRule.getName(), promotionRule);
        });
    }

    @Override
    public void save(ItemSaveRequest itemSaveRequest) {
        List<Item> itemList = new ArrayList<>();
        List<PromotionItem> promotionItemList = new ArrayList<>();

        separateItemRequests(itemSaveRequest, promotionItemList, itemList);
        addItem(promotionItemList, itemList, itemSaveRequest);
        itemList.forEach(this::saveGeneralItem);
        promotionItemList.forEach(this::savePromotionItem);
    }

    private void separateItemRequests(ItemSaveRequest itemSaveRequest, List<PromotionItem> promotionItems,
                                      List<Item> itemList) {
        for (ItemRequest itemRequest : itemSaveRequest.getItems()) {
            if (!isPromotionItem(itemRequest)) {
                itemList.add(Item.create(idHolder, itemRequest));
                continue;
            }
            addPromotionItem(promotionItems, itemRequest);
        }
    }

    private void addPromotionItem(List<PromotionItem> promotionItems, ItemRequest itemRequest) {
        PromotionRule promotionRule = promotionRuleRepository.findByName(
                        itemRequest.getPromotionRule().orElseThrow(() -> new ServiceArgumentException(RULE_NOT_FOUND)))
                .orElseThrow(() -> new ServiceArgumentException(RULE_NOT_FOUND));
        PromotionItem promotionItem = PromotionItem.create(idHolder, itemRequest, promotionRule);
        checkPromotionActive(promotionItems, promotionItem);
    }

    private void checkPromotionActive(List<PromotionItem> promotionItems, PromotionItem promotionItem) {
        if (promotionItem.isActive(DateTimes.now())) {
            promotionItems.add(promotionItem);
        }
    }

    private void addItem(List<PromotionItem> promotionItems, List<Item> items, ItemSaveRequest itemSaveRequest) {
        promotionItems.forEach(promotionItem -> addItemIfNotExists(promotionItem, items, itemSaveRequest));
    }

    private void addItemIfNotExists(PromotionItem promotionItem, List<Item> items, ItemSaveRequest itemSaveRequest) {
        boolean itemExists = items.stream().anyMatch(i -> i.getName().equals(promotionItem.getName()));

        if (!itemExists) {
            long price = getPriceFromRequest(itemSaveRequest, promotionItem);
            items.add(Item.createMock(idHolder, promotionItem.getName(), price));
        }
    }

    private Long getPriceFromRequest(ItemSaveRequest itemSaveRequest, PromotionItem promotionItem) {
        return itemSaveRequest.getItems().stream().filter(i -> i.getName().equals(promotionItem.getName())).findFirst()
                .orElseThrow(() -> new ServiceArgumentException(ITEM_NOT_FOUND)).getPrice();
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
