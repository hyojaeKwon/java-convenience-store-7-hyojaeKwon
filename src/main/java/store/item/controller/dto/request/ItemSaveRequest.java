package store.item.controller.dto.request;

import java.util.List;

public class ItemSaveRequest {
    private final List<ItemRequest> items;

    public ItemSaveRequest(List<ItemRequest> items) {
        this.items = items;
    }

    public List<ItemRequest> getItems() {
        return items;
    }
}
