package store.item.controller;

import java.util.List;
import store.item.domain.item.value.ItemInfo;

public interface ItemStockService {
    void updateItemStock(ItemInfo itemInfo);

    List<ItemInfo> getAllItemInfo();

    ItemInfo getItemInfoByName(String name);
}
