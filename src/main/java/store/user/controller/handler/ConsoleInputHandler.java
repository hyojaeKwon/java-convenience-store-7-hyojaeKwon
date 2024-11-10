package store.user.controller.handler;

import store.purchase.controller.dto.request.purchase.PurchaseRequest;
import store.user.controller.parser.InputParser;
import store.user.controller.provider.InputProvider;
import store.user.controller.validator.Validator;

public class ConsoleInputHandler {

    private final InputProvider inputProvider;
    private final InputParser parser;

    public ConsoleInputHandler(InputProvider inputProvider, InputParser parser) {
        this.inputProvider = inputProvider;
        this.parser = parser;
    }

    // 입력 받아서 구매 객체로 parse
    public PurchaseRequest purchaseInput() {
        String purchaseRawInput = inputProvider.readLine();
        Validator.validateString(purchaseRawInput);
        return parser.parseStringToPurchaseRequest(purchaseRawInput);
    }

    // Y 나 N 입력받기
    public boolean controlInput() {
        String control = inputProvider.readLine();
        return parser.parseControlStringToBoolean(control);
    }

}
