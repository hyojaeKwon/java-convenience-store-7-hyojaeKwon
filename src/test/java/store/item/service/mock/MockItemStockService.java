package store.item.service.mock;

import java.util.List;
import java.util.Optional;
import store.common.util.SystemUuidHolder;
import store.item.controller.ItemStockService;
import store.item.controller.dto.request.ItemRequest;
import store.item.domain.item.Item;
import store.item.domain.item.value.ItemInfo;

public class MockItemStockService implements ItemStockService {
    @Override
    public void updateItemStock(ItemInfo itemInfo) {
    }

    @Override
    public List<ItemInfo> getAllItemInfo() {
        Item item = Item.create(new SystemUuidHolder(), new ItemRequest("item", 100, 100, Optional.empty()));
        return List.of(ItemInfo.createNotPromotionItemInfo(item));
    }

    @Override
    public ItemInfo getItemInfoByName(String name) {
        Item item = Item.create(new SystemUuidHolder(), new ItemRequest("item", 100, 100, Optional.empty()));
        return ItemInfo.createNotPromotionItemInfo(item);
    }
}
