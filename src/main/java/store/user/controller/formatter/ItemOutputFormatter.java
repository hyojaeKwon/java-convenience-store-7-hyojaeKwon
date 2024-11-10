package store.user.controller.formatter;

import java.util.List;
import store.item.domain.item.Item;
import store.item.domain.item.PromotionItem;
import store.item.domain.item.value.ItemInfo;

public class ItemOutputFormatter {

    public static final String OUT_OF_STOCK = "재고 없음 ";
    public static final String ITEM_UNIT = "개 ";
    public static final String MONEY_FORMAT = "%,d";
    public static final String MONEY_UNIT = "원 ";
    public static final String ONE_BLANK = " ";
    public static final String HYPHEN = "- ";
    public static final String LINE_SP = "\n";

    public String format(List<ItemInfo> itemInfos) {
        StringBuilder sb = new StringBuilder();
        for (ItemInfo itemInfo : itemInfos) {
            formatItemInfo(itemInfo, sb);
        }
        return sb.toString();
    }

    private void formatItemInfo(ItemInfo itemInfo, StringBuilder sb) {
        formatItem(itemInfo.getItem(), sb);
        sb.append(LINE_SP);
        if (itemInfo.isPromotion()) {
            formatPromotionItem(itemInfo.getItem(), itemInfo.getPromotionItem(), sb);
            sb.append(LINE_SP);
        }
    }

    private void formatPromotionItem(Item item, PromotionItem promotionItem, StringBuilder sb) {
        formatItemName(promotionItem.getName(), sb);
        formatItemPrice(item.getPrice(), sb);
        formatStockAmount(promotionItem.getStockQuantity(), sb);
        sb.append(promotionItem.getPromotionRule().getName());
    }

    private void formatItem(Item item, StringBuilder sb) {
        formatItemName(item.getName(), sb);
        formatItemPrice(item.getPrice(), sb);
        formatStockAmount(item.getStockQuantity(), sb);
    }

    private void formatItemName(String name, StringBuilder sb) {
        sb.append(HYPHEN);
        sb.append(name).append(ONE_BLANK);
    }

    private void formatItemPrice(long price, StringBuilder sb) {
        String format = String.format(MONEY_FORMAT, price);
        sb.append(format);
        sb.append(MONEY_UNIT);
    }

    private void formatStockAmount(long stockAmount, StringBuilder sb) {
        if (stockAmount > 0) {
            sb.append(stockAmount).append(ITEM_UNIT);
            return;
        }
        sb.append(OUT_OF_STOCK);
    }
}
