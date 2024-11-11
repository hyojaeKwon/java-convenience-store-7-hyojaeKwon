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

    public PurchaseRequest purchaseInput() {
        String purchaseRawInput = inputProvider.readLine();
        Validator.validateString(purchaseRawInput);
        return parser.parseStringToPurchaseRequest(purchaseRawInput);
    }

    public boolean controlInput() {
        String control = inputProvider.readLine();
        Validator.validateString(control);
        return parser.parseControlStringToBoolean(control);
    }

}
