package store.user.controller.parser;

import static store.common.exception.input.InputException.NOT_VALID_CONTROL;
import static store.common.exception.input.InputException.NOT_VALID_DATE;
import static store.common.exception.input.InputException.NOT_VALID_INPUT;
import static store.common.exception.input.InputException.NOT_VALID_PARSE;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.regex.PatternSyntaxException;
import store.common.exception.input.InputException;
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
            throw new InputException(NOT_VALID_PARSE);
        }
    }

    public long parseStringToLong(String target) {
        try {
            return Long.parseLong(target.trim());
        } catch (NumberFormatException e) {
            throw new InputException(NOT_VALID_PARSE);
        }
    }

    public LocalDateTime parseStringToDate(String target) {
        DateTimeFormatter dayFormat = DateTimeFormatter.ofPattern(DATE_FORMAT);
        try {
            LocalDate nowDate = LocalDate.parse(target, dayFormat);
            return nowDate.atStartOfDay();
        } catch (DateTimeParseException e) {
            throw new InputException(NOT_VALID_DATE);
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
        if (target.equals(CONTROL_DISAGREE)) {
            return false;
        }
        throw new InputException(NOT_VALID_CONTROL);
    }

    private PurchaseItemRequest extractInnerRequestValue(String request) {
        request = request.trim();
        if (!request.startsWith(PURCHASE_START_PREFIX) || !request.endsWith(PURCHASE_END_PREFIX)) {
            throw new InputException(NOT_VALID_INPUT);
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
