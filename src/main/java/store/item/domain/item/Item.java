package store.item.domain.item;

import store.common.util.IdHolder;
import store.item.controller.dto.request.ItemRequest;

public class Item {

    private final String id;
    private final String name;
    private final long price;
    private final long stockQuantity;
    private final boolean isPromotion;

    private Item(String id, String name, long price, long stockQuantity, boolean isPromotion) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.isPromotion = isPromotion;
    }

    // static factory method

    public static Item of(IdHolder idHolder, String name, long price, long stockQuantity, boolean isPromotion) {
        return new Item(idHolder.generateId(), name, price, stockQuantity, isPromotion);
    }

    public static Item create(IdHolder idHolder, ItemRequest itemRequest) {
        return new Item(idHolder.generateId(), itemRequest.getName(), itemRequest.getPrice(),
                itemRequest.getQuantity(), false);
    }

    // factory method

    public Item purchase(long quantity) {
        if (quantity > stockQuantity) {
            throw new IllegalArgumentException();
        }
        return new Item(id, name, price, stockQuantity - quantity, isPromotion);
    }

    public Item closePromotion() {
        return new Item(id, name, price, stockQuantity, false);
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

    public boolean isPurchasable(long buyQuantity) {
        return stockQuantity >= buyQuantity;
    }

    public boolean isPromotion() {
        return isPromotion;
    }
}
