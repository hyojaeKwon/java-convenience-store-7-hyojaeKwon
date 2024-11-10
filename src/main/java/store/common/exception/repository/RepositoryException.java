package store.common.exception.repository;

public class RepositoryException extends IllegalArgumentException {

    public static final String ID_NULL = "구분자 값이 존재하지 않습니다.";
    public static final String VALUE_NULL = "저장하려는 상품이 존재하지 않습니다.";
    public static final String NOT_FOUND = "존재하지 않는 상품입니다.";
    public static final String NOT_FOUND_PROMOTION = "존재하지 않는 프로모션 입니다.";

    public RepositoryException(String s) {
        super(s);
    }
}
