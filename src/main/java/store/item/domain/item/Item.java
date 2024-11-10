package store.item.domain.item;

import static store.common.exception.domain.DomainArgumentException.EXCEED_AMOUNT;
import static store.common.exception.domain.DomainArgumentException.NEG_AMOUNT;

import store.common.exception.domain.DomainArgumentException;
import store.common.util.IdHolder;
import store.item.controller.dto.request.ItemRequest;

public class Item {

    private final String id;
    private final String name;
    private final long price;
    private final long stockQuantity;

    private Item(String id, String name, long price, long stockQuantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    public static Item create(IdHolder idHolder, ItemRequest itemRequest) {
        return new Item(idHolder.generateId(), itemRequest.getName(), itemRequest.getPrice(),
                itemRequest.getQuantity());
    }

    public Item purchase(long quantity) {
        if (quantity < 0L) {
            throw new DomainArgumentException(NEG_AMOUNT);
        }
        if (quantity > stockQuantity) {
            throw new DomainArgumentException(EXCEED_AMOUNT);
        }
        return new Item(id, name, price, stockQuantity - quantity);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getPrice() {
        return price;
    }

    public long getStockQuantity() {
        return stockQuantity;
    }

}
