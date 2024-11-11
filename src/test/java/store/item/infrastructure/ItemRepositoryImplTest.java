package store.item.infrastructure;

import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.common.util.IdHolder;
import store.item.controller.dto.request.ItemRequest;
import store.item.domain.item.Item;

class ItemRepositoryImplTest {

    private ItemRepositoryImpl itemRepository;

    private Item item1;
    private Item item2;
    private Item nullItem;

    @BeforeEach
    void setUp() {
        IdHolder nullHolder = new NullIdHolder();
        IdHolder testHolder = new TestHolder();

        ItemRequest itemRequest1 = new ItemRequest("item1", 100, 100, Optional.empty());
        ItemRequest itemRequest2 = new ItemRequest("item2", 100, 100, Optional.empty());

        item1 = Item.create(testHolder, itemRequest1);
        item2 = Item.create(testHolder, itemRequest2);
        nullItem = Item.create(nullHolder, itemRequest1);

        itemRepository = (ItemRepositoryImpl) ItemRepositoryImpl.getInstance();
        itemRepository.findAll().forEach(item -> itemRepository.delete(item.getId()));
    }

    @Test
    @DisplayName("새로운 아이템을 저장하면 해당 아이템을 반환한다")
    void testSave() {
        Item savedItem = itemRepository.save(item1.getId(), item1);

        Assertions.assertThat(savedItem).isEqualTo(item1);
        Assertions.assertThat(itemRepository.findById(item1.getId())).isEqualTo(item1);
    }

    @Test
    @DisplayName("ID가 null인 경우 저장 시 예외를 발생시킨다")
    void testSaveWithNullIdThrowsException() {
        Assertions.assertThatThrownBy(() -> itemRepository.save(nullItem.getId(), nullItem))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("아이템을 ID로 조회하면 해당 아이템을 반환한다")
    void testFindById() {
        itemRepository.save(item1.getId(), item1);

        Item foundItem = itemRepository.findById(item1.getId());
        Assertions.assertThat(foundItem).isEqualTo(item1);
    }

    @Test
    @DisplayName("존재하지 않는 ID로 조회 시 예외를 발생시킨다")
    void testFindByIdNotFoundThrowsException() {
        Assertions.assertThatThrownBy(() -> itemRepository.findById("non-existing-id"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("아이템을 업데이트하면 업데이트된 아이템을 반환한다")
    void testUpdate() {
        itemRepository.save(item1.getId(), item1);

        Item updatedItem1 = item1.purchase(1);
        Item result = itemRepository.update(item1.getId(), updatedItem1);

        Assertions.assertThat(result).isEqualTo(updatedItem1);
        Assertions.assertThat(itemRepository.findById(updatedItem1.getId())).isEqualTo(updatedItem1);
    }

    @Test
    @DisplayName("아이템 이름으로 조회하면 해당 아이템을 반환한다")
    void testFindByName() {
        itemRepository.save(item1.getId(), item1);

        Optional<Item> foundItem = itemRepository.findByName(item1.getName());

        Assertions.assertThat(foundItem).isPresent();
        Assertions.assertThat(foundItem).contains(item1);
    }

    @Test
    @DisplayName("존재하지 않는 이름으로 조회하면 빈 Optional을 반환한다")
    void testFindByNameNotFound() {
        Optional<Item> foundItem = itemRepository.findByName("Non-existing Item");

        Assertions.assertThat(foundItem).isNotPresent();
    }

    @Test
    @DisplayName("아이템을 삭제하면 삭제된 아이템을 반환한다")
    void testDelete() {
        itemRepository.save(item1.getId(), item1);

        Item deletedItem = itemRepository.delete(item1.getId());

        Assertions.assertThat(deletedItem).isEqualTo(item1);
        Assertions.assertThatThrownBy(() -> itemRepository.findById(item1.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("저장된 모든 아이템을 반환한다")
    void testFindAll() {
        itemRepository.save(item1.getId(), item1);
        itemRepository.save(item2.getId(), item2);

        List<Item> items = itemRepository.findAll();

        Assertions.assertThat(items.getFirst().getId()).isEqualTo(item1.getId());
        Assertions.assertThat(items.getLast().getId()).isEqualTo(item2.getId());
    }

    static class NullIdHolder implements IdHolder {
        @Override
        public String generateId() {
            return null;
        }
    }

    static class TestHolder implements IdHolder {
        @Override
        public String generateId() {
            return "test";
        }
    }
}