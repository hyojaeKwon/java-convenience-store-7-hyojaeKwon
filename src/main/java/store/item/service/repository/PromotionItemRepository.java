package store.item.service.repository;

import java.util.List;
import store.item.domain.item.PromotionItem;

public interface PromotionItemRepository extends MapRepository<String, PromotionItem> {

    List<PromotionItem> findAll();
}
