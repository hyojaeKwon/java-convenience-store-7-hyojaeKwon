package store.user.controller.handler;

public class ConsoleOutputHandler {

    private static final String GREETING = "안녕하세요. W편의점입니다.";
    private static final String GREETING_STOCK = "현재 보유하고 있는 상품입니다.";
    private static final String PURCHASE_MESSAGE = "구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])";
    private static final String MEMBERSHIP = "멤버십 할인을 받으시겠습니까? (Y/N)";
    private static final String MORE_PURCHASE = "감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)";

    public void printGreeting() {
        print(GREETING);
        newLine();
        print(GREETING_STOCK);
        newLineTwice();
    }

    public void printStockInfo(String stockInfo) {
        print(stockInfo);
        newLine();
    }

    public void printPurchaseMessage() {
        print(PURCHASE_MESSAGE);
        newLine();
    }

    public void printContextMessage(String contextMessage) {
        print(contextMessage);
        newLine();
    }

    public void printMemberShip() {
        print(MEMBERSHIP);
        newLine();
    }

    public void morePurchaseMessage() {
        print(MORE_PURCHASE);
        newLine();
    }

    private void print(String message) {
        System.out.print(message);
    }

    private void newLine() {
        System.out.println();
    }

    private void newLineTwice() {
        newLine();
        newLine();
    }
}
