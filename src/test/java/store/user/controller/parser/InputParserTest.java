package store.user.controller.parser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.common.exception.input.InputException;
import store.purchase.controller.dto.request.purchase.PurchaseItemRequest;
import store.purchase.controller.dto.request.purchase.PurchaseRequest;

class InputParserTest {

    private InputParser parser;

    @BeforeEach
    void setUp() {
        parser = new InputParser();
    }

    @Test
    @DisplayName("문자열을 구분자로 나누어 리스트로 반환한다.")
    void testParseStringToList() {
        String input = "aaa,bbb,ccc";
        List<String> result = parser.parseStringToList(input);
        assertThat(result).containsExactly("aaa", "bbb", "ccc");
    }

    @Test
    @DisplayName("문자열을 Long으로 변환한다")
    void testParseStringToLong() {
        String input = "12345";
        long result = parser.parseStringToLong(input);
        assertThat(result).isEqualTo(12345L);
    }

    @Test
    @DisplayName("Long으로 변환할 수 없는 문자열일 경우 예외를 발생시킨다")
    void testParseStringToLongWithInvalidNumber() {
        String input = "aaa";
        assertThatThrownBy(() -> parser.parseStringToLong(input)).isInstanceOf(InputException.class);
    }

    @Test
    @DisplayName("유효한 날짜 문자열을 LocalDateTime으로 변환한다")
    void testParseStringToDate() {
        String input = "2023-11-11";
        LocalDateTime result = parser.parseStringToDate(input);
        assertThat(result).isEqualTo(LocalDateTime.of(2023, 11, 11, 0, 0));
    }

    @Test
    @DisplayName("잘못된 날짜 형식의 문자열일 경우 예외를 발생시킨다")
    void testParseStringToDateWithInvalidDate() {
        String input = "11-11-2023";
        assertThatThrownBy(() -> parser.parseStringToDate(input)).isInstanceOf(InputException.class);
    }

    @Test
    @DisplayName("유효한 문자열을 PurchaseRequest로 변환한다")
    void testParseStringToPurchaseRequest() {
        String input = "[item1-10],[item2-5]";
        PurchaseRequest result = parser.parseStringToPurchaseRequest(input);

        PurchaseItemRequest itemRequest1 = new PurchaseItemRequest("item1", 10);
        PurchaseItemRequest itemRequest2 = new PurchaseItemRequest("item2", 5);

        assertThat(itemRequest1.getName()).isEqualTo(result.getPurchaseItems().get(0).getName());
        assertThat(itemRequest1.getQuantity()).isEqualTo(result.getPurchaseItems().get(0).getQuantity());
        assertThat(itemRequest2.getName()).isEqualTo(result.getPurchaseItems().get(1).getName());
        assertThat(itemRequest2.getQuantity()).isEqualTo(result.getPurchaseItems().get(1).getQuantity());
    }

    @Test
    @DisplayName("잘못된 형식의 문자열을 변환할 경우 예외를 발생시킨다")
    void testParseStringToPurchaseRequestWithInvalidFormat() {
        String input = "item1-10,item2-5";
        assertThatThrownBy(() -> parser.parseStringToPurchaseRequest(input)).isInstanceOf(InputException.class);
    }

    @Test
    @DisplayName("Y/N 문자열을 Boolean으로 변환한다")
    void testParseControlStringToBoolean() {
        assertThat(parser.parseControlStringToBoolean("Y")).isTrue();
        assertThat(parser.parseControlStringToBoolean("N")).isFalse();
    }

    @Test
    @DisplayName("Y/N 이외의 문자열일 경우 예외를 발생시킨다")
    void testParseControlStringToBooleanWithInvalidInput() {
        assertThatThrownBy(() -> parser.parseControlStringToBoolean("y")).isInstanceOf(InputException.class);
    }
}