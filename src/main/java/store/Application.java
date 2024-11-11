package store;

public class Application {

    public static final String STATE_ERROR_PREFIX = "[ERROR]  ";

    public static void main(String[] args) {
        try {
            MainConfig mainConfig = new MainConfig();
            mainConfig.run();
        } catch (IllegalStateException e) {
            System.out.println(STATE_ERROR_PREFIX + e.getMessage());
        }
    }
}
