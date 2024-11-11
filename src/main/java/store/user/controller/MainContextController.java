package store.user.controller;

import java.util.List;
import store.item.controller.ItemController;
import store.item.domain.item.value.ItemInfo;
import store.user.controller.formatter.ItemOutputFormatter;
import store.user.controller.handler.ConsoleInputHandler;
import store.user.controller.handler.ConsoleOutputHandler;
import store.user.controller.handler.retry.RetryHandler;
import store.user.controller.input.InputController;

public class MainContextController {

    private final InputController inputController;
    private final ItemController itemController;
    private final ConsoleInputHandler inputHandler;
    private final ConsoleOutputHandler outputHandler;
    private final ItemOutputFormatter itemFormatter;
    private final PurchaseContextHandler purchaseContextHandler;

    public MainContextController(InputController inputController, ItemController itemController,
                                 ConsoleInputHandler inputHandler, ConsoleOutputHandler outputHandler,
                                 ItemOutputFormatter itemFormatter, PurchaseContextHandler purchaseContextHandler) {
        this.inputController = inputController;
        this.itemController = itemController;
        this.inputHandler = inputHandler;
        this.outputHandler = outputHandler;
        this.itemFormatter = itemFormatter;
        this.purchaseContextHandler = purchaseContextHandler;
    }

    public void run() {
        saveResources();
        executeStoreLoop();
    }

    private void executeStoreLoop() {
        while (runPurchase()) {
        }
    }

    private void saveResources() {
        inputController.saveResources();
    }

    private boolean runPurchase() {
        displayAvailableItems();
        purchaseContextHandler.run();
        outputHandler.morePurchaseMessage();
        return controlMoreTrial();
    }

    private boolean controlMoreTrial() {
        return RetryHandler.retry(inputHandler::controlInput);
    }

    private void displayAvailableItems() {
        List<ItemInfo> allItemInfo = itemController.getAllItemInfo();
        String stockInfo = itemFormatter.format(allItemInfo);
        outputHandler.printGreeting();
        outputHandler.printStockInfo(stockInfo);
    }
}
