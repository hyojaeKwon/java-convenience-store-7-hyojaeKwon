package store.user.controller.parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.PatternSyntaxException;
import store.purchase.controller.dto.request.purchase.PurchaseItemRequest;
import store.purchase.controller.dto.request.purchase.PurchaseRequest;
import store.user.controller.validator.Validator;

public class InputParser {

    private static final String LIST_DELIMITER = ",";
    private static final String CONTROL_AGREE = "Y";
    private static final String CONTROL_DISAGREE = "N";
    private static final String PURCHASE_START_PREFIX = "[";
    private static final String PURCHASE_END_PREFIX = "]";
    private static final String PURCHASE_DELIMITER = "-";
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    public List<String> parseStringToList(String target) {
        try {
            return List.of(target.trim().split(LIST_DELIMITER));
        } catch (PatternSyntaxException e) {
            throw new IllegalArgumentException("pattern syntax이 유효하지 않음");
        }
    }

    public long parseStringToLong(String target) {
        try {
            return Long.parseLong(target.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("입력값이 long으로 변환될 수 업습니다.");
        }
    }

    public Date parseStringToDate(String target) {
        SimpleDateFormat dayFormat = new SimpleDateFormat(DATE_FORMAT);
        try {
            return dayFormat.parse(target);
        } catch (ParseException e) {
            throw new IllegalArgumentException("날짜 형식이 유효하지 않습니다.");
        }
    }

    public PurchaseRequest parseStringToPurchaseRequest(String target) {
        List<String> parsedRequest = parseStringToList(target);
        List<PurchaseItemRequest> itemRequests = parsedRequest.stream().map(this::extractInnerRequestValue).toList();
        return new PurchaseRequest(itemRequests);
    }

    public boolean parseControlStringToBoolean(String target) {
        if (target.equals(CONTROL_AGREE)) {
            return true;
        }
        if(target.equals(CONTROL_DISAGREE)) {
            return false;
        }
        throw new IllegalArgumentException("y/n 중에 하나 입력해야함");
    }

    private PurchaseItemRequest extractInnerRequestValue(String request) {
        request = request.trim();
        if (!request.startsWith(PURCHASE_START_PREFIX) || !request.endsWith(PURCHASE_END_PREFIX)) {
            throw new IllegalArgumentException("구매 입력 형식이 유효하지 않습니다.");
        }
        request = request.substring(1, request.length() - 1);
        return parseStringToPurchaseItemRequest(request);
    }

    private PurchaseItemRequest parseStringToPurchaseItemRequest(String request) {
        String[] split = request.split(PURCHASE_DELIMITER);
        String name = split[0];
        Validator.validateString(name);
        long amount = parseStringToLong(split[1]);
        return new PurchaseItemRequest(name, amount);
    }
}
