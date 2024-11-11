package store.user.controller;

import store.common.util.IdHolder;
import store.common.util.SystemUuidHolder;
import store.item.config.ItemConfig;
import store.purchase.config.PurchaseConfig;
import store.user.controller.formatter.ItemOutputFormatter;
import store.user.controller.formatter.PromotionOutputFormatter;
import store.user.controller.formatter.ReceiptOutputFormatter;
import store.user.controller.handler.ConsoleInputHandler;
import store.user.controller.handler.ConsoleOutputHandler;
import store.user.controller.input.InputController;
import store.user.controller.input.converter.ResourceItemConverter;
import store.user.controller.input.converter.ResourcePromotionConverter;
import store.user.controller.input.reader.InputFileReader;
import store.user.controller.parser.InputParser;
import store.user.controller.provider.ConsoleInputProvider;
import store.user.controller.provider.InputProvider;

public class UserConfig {

    private final ItemConfig itemConfig;
    private final PurchaseConfig purchaseConfig;

    private final ConsoleOutputHandler consoleOutputHandler = new ConsoleOutputHandler();
    private final InputParser inputParser = new InputParser();
    private final InputProvider inputProvider = new ConsoleInputProvider();
    private final IdHolder idHolder = new SystemUuidHolder();

    public UserConfig(ItemConfig itemConfig, PurchaseConfig purchaseConfig) {
        this.itemConfig = itemConfig;
        this.purchaseConfig = purchaseConfig;
    }

    public MainContextController mainContextController() {
        return new MainContextController(inputController(), itemConfig.itemController(), consoleInputHandler(),
                consoleOutputHandler, itemOutputFormatter(), purchaseContextHandler());
    }

    private InputController inputController() {
        return new InputController(itemConfig.itemController(), resourcePromotionConverter(), resourceItemConverter());
    }

    private PurchaseContextHandler purchaseContextHandler() {
        return new PurchaseContextHandler(consoleInputHandler(), purchaseConfig.purchaseController(),
                promotionOutputFormatter(), consoleOutputHandler, receiptOutputFormatter(), idHolder);
    }

    private ItemOutputFormatter itemOutputFormatter() {
        return new ItemOutputFormatter();
    }

    private PromotionOutputFormatter promotionOutputFormatter() {
        return new PromotionOutputFormatter();
    }

    private ReceiptOutputFormatter receiptOutputFormatter() {
        return new ReceiptOutputFormatter();
    }

    private ConsoleInputHandler consoleInputHandler() {
        return new ConsoleInputHandler(inputProvider, inputParser);
    }

    private ResourceItemConverter resourceItemConverter() {
        return new ResourceItemConverter(inputFileReader(), inputParser);
    }

    private ResourcePromotionConverter resourcePromotionConverter() {
        return new ResourcePromotionConverter(inputFileReader(), inputParser);
    }

    private InputFileReader inputFileReader() {
        return new InputFileReader();
    }
}