package store.user.controller.formatter;

import store.purchase.domain.discount.promotion.PromotionDiscountItem;
import store.purchase.domain.value.PurchaseItem;
import store.purchase.domain.value.receipt.Receipt;
import store.purchase.domain.value.receipt.ReceiptPrice;

public class ReceiptOutputFormatter {

    private static final String RECEIPT_HEADER = "==============W 편의점================";
    private static final String PROMOTION_HEADER = "=============증     정===============";
    private static final String ITEM_HEADER = String.format("%-19s%-10s%-6s", "상품명", "수량", "금액");
    private static final String PRICE_HEADER = "====================================";

    private static final String ITEM_FORMAT = "%-19s%-10d%-,6d";
    private static final String PROMOTION_FORMAT = "%-19s%-,16d";
    private static final String TOTAL_PRICE_FORMAT = "%-19s%-10d%-,6d";
    private static final String PRICE_FORMAT = "%-19s%-10s%s%-,5d";

    private static final String NEW_LINE = "\n";
    private static final String EMPTY_STRING = "";
    private static final String BLANK_STRING = " ";
    private static final String HYPHEN = "-";

    private static final String TOTAL_PRICE_KOREAN = "총구매액";
    private static final String PROMOTION_DISCOUNT_KOREAN = "행사할인";
    private static final String MEMBERSHIP_DISCOUNT_KOREAN = "멤버십할인";
    private static final String FINAL_PRICE_KOREAN = "내실돈";


    public String format(Receipt receipt) {
        StringBuilder sb = new StringBuilder();
        formatReceiptHeader(receipt, sb);
        formatPromotionHeader(receipt, sb);
        formatPriceHeader(receipt, sb);
        return sb.toString();
    }

    private void formatPriceHeader(Receipt receipt, StringBuilder sb) {
        sb.append(PRICE_HEADER).append(NEW_LINE);
        formatPrice(receipt, sb);
        formatDiscountPrice(receipt.getPrice(), sb);
        formatTotalPrice(receipt.getPrice(), sb);
    }

    private void formatPrice(Receipt receipt, StringBuilder sb) {
        String format = String.format(TOTAL_PRICE_FORMAT, TOTAL_PRICE_KOREAN, receipt.totalPurchaseAmount(),
                receipt.getPrice().getTotalPrice());
        sb.append(format).append(NEW_LINE);
    }

    private void formatDiscountPrice(ReceiptPrice price, StringBuilder sb) {
        String promotionFormat = String.format(PRICE_FORMAT, PROMOTION_DISCOUNT_KOREAN, EMPTY_STRING, HYPHEN,
                price.getPromotionDiscountPrice());
        String membershipFormat = String.format(PRICE_FORMAT, MEMBERSHIP_DISCOUNT_KOREAN, EMPTY_STRING, HYPHEN,
                price.getMembershipDiscountPrice());
        sb.append(promotionFormat).append(NEW_LINE);
        sb.append(membershipFormat).append(NEW_LINE);
    }

    private void formatTotalPrice(ReceiptPrice price, StringBuilder sb) {
        String format = String.format(PRICE_FORMAT, FINAL_PRICE_KOREAN, EMPTY_STRING, BLANK_STRING,
                price.getResultPrice());
        sb.append(format).append(NEW_LINE);
    }


    private void formatReceiptHeader(Receipt receipt, StringBuilder sb) {
        sb.append(RECEIPT_HEADER).append(NEW_LINE);
        sb.append(ITEM_HEADER).append(NEW_LINE);
        for (PurchaseItem item : receipt.getPurchaseItems()) {
            formatPurchaseItem(item, sb);
        }
    }

    private void formatPromotionHeader(Receipt receipt, StringBuilder sb) {
        if (receipt.getPromotionDiscountItems().isEmpty()) {
            return;
        }
        sb.append(PROMOTION_HEADER).append(NEW_LINE);
        for (PromotionDiscountItem item : receipt.getPromotionDiscountItems()) {
            formatPromotionItem(item, sb);
        }
    }

    private void formatPromotionItem(PromotionDiscountItem item, StringBuilder sb) {
        String format = String.format(PROMOTION_FORMAT, item.getName(), item.getDiscountAmount());
        sb.append(format).append(NEW_LINE);
    }

    private void formatPurchaseItem(PurchaseItem item, StringBuilder sb) {
        String format = String.format(ITEM_FORMAT, item.getName(), item.getAmount(), item.getTotalPrice());
        sb.append(format).append(NEW_LINE);
    }
}

