package store;

import store.item.config.ItemConfig;
import store.purchase.config.PurchaseConfig;
import store.user.controller.UserConfig;

public class MainConfig {

    public void run() {
        userConfig().mainContextController().run();
    }

    private UserConfig userConfig() {
        return new UserConfig(itemConfig(), purchaseConfig());
    }

    private ItemConfig itemConfig() {
        return new ItemConfig();
    }

    private PurchaseConfig purchaseConfig() {
        return new PurchaseConfig(itemConfig());
    }

}
