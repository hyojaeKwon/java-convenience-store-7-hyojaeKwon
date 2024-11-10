package store.user.controller.input.converter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import store.user.controller.input.reader.InputFileReader;
import store.user.controller.parser.InputParser;
import store.item.controller.dto.request.PromotionRuleRequest;

public class ResourcePromotionConverter {
    private static final int BUY_QUANTITY_INDEX = 1;
    private static final int GET_QUANTITY_INDEX = 2;
    private static final int START_DAY_INDEX = 3;
    private static final int END_DAY_INDEX = 4;

    private final InputFileReader inputFileReader;
    private final InputParser parser;

    public ResourcePromotionConverter(InputFileReader inputFileReader, InputParser parser) {
        this.inputFileReader = inputFileReader;
        this.parser = parser;
    }

    public List<PromotionRuleRequest> getRuleRequest() {
        List<String> rules = inputFileReader.readRule();
        List<PromotionRuleRequest> promotionRuleRequests = new ArrayList<>();
        removeHeader(rules);
        addRequest(rules, promotionRuleRequests);
        return promotionRuleRequests;
    }

    private void addRequest(List<String> rules, List<PromotionRuleRequest> promotionRuleRequests) {
        rules.forEach(ruleStr -> {
            List<String> ruleList = parser.parseStringToList(ruleStr);
            promotionRuleRequests.add(convertToRequest(ruleList));
        });
    }

    private void removeHeader(List<String> rules) {
        rules.removeFirst();
    }

    private PromotionRuleRequest convertToRequest(List<String> rule) {
        String name = rule.getFirst();
        long buyQuantity = parser.parseStringToLong(rule.get(BUY_QUANTITY_INDEX));
        long getQuantity = parser.parseStringToLong(rule.get(GET_QUANTITY_INDEX));
        Date startDate = parser.parseStringToDate(rule.get(START_DAY_INDEX));
        Date endDate = parser.parseStringToDate(rule.get(END_DAY_INDEX));
        return new PromotionRuleRequest(name, buyQuantity, getQuantity, startDate, endDate);
    }
}
