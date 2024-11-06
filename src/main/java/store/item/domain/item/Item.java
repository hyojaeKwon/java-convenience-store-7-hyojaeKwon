package store.item.domain.item;

public class Item {

    private final String id;
    private final String name;
    private final int price;
    private final long stockQuantity;

    public Item(String id, String name, int price, long stockQuantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public long getStockQuantity() {
        return stockQuantity;
    }

    public boolean isPurchasable(long buyQuantity) {
        return stockQuantity >= buyQuantity;
    }
}
