package store.user.controller.input.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import store.user.controller.input.reader.InputFileReader;
import store.user.controller.parser.InputParser;
import store.item.controller.dto.request.ItemRequest;
import store.item.controller.dto.request.ItemSaveRequest;

public class ResourceItemConverter {
    private static final int NAME_INDEX = 0;
    private static final int PRICE_INDEX = 1;
    private static final int STOCK_INDEX = 2;
    private static final int PROMOTION_INDEX = 3;

    private final InputFileReader inputFileReader;
    private final InputParser parser;

    public ResourceItemConverter(InputFileReader inputFileReader, InputParser parser) {
        this.inputFileReader = inputFileReader;
        this.parser = parser;
    }

    public ItemSaveRequest getItemSaveRequest() {
        List<String> itemRawString = inputFileReader.readItem();
        List<ItemRequest> requests = new ArrayList<>();
        removeHeader(itemRawString);
        addRequest(itemRawString, requests);
        return new ItemSaveRequest(requests);
    }

    private void addRequest(List<String> itemRawString, List<ItemRequest> requests) {
        itemRawString.forEach(itemStr -> {
            List<String> itemList = parser.parseStringToList(itemStr);
            requests.add(convertToRequest(itemList));
        });
    }

    private void removeHeader(List<String> rules) {
        rules.removeFirst();
    }

    private ItemRequest convertToRequest(List<String> itemList) {
        String name = itemList.get(NAME_INDEX);
        long price = parser.parseStringToLong(itemList.get(PRICE_INDEX));
        long stock = parser.parseStringToLong(itemList.get(STOCK_INDEX));
        Optional<String> promotion = checkPromotionExists(itemList);
        return new ItemRequest(name, price, stock, promotion);
    }

    private Optional<String> checkPromotionExists(List<String> itemList) {
        String promotion;
        try {
            promotion = itemList.get(PROMOTION_INDEX);
        } catch (IndexOutOfBoundsException e) {
            return Optional.empty();
        }
        if (promotion == null || promotion.isEmpty() || promotion.equals("null")) {
            return Optional.empty();
        }
        return Optional.of(promotion);
    }
}
