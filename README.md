# java-convenience-store-precourse

# 설계 방향
## Domain이 주가 되자

1. 이번 미션은 Domain Driven Design의 방식을 최대한 따라가보고자 노력했습니다.
2. 전 도메인을 불변 객체로 사용하여 안정성을 높이고, 상태가 바뀌는 행위에 대해서는 새로운 객체를 생성하여 반환했습니다.
3. 각 도메인은 모두 책임을 갖게 하고, 정보 전달 및 책임에 해당하는 로직을 넣고자 하였습니다.
4. 루트 도메인을 활용하여 도메인안에서 로직을 수행해 정보를 조작하고 결과를 도출하였습니다. 
5. 또한, domain의 안정성을 높이기 위해 생성자를 사용하는 것을 지양하고, static factory method와 dto를 통하여 객체를 생성하도록 하였습니다.

## Service에서는 Domain로직을 호출하자

1. Service layer에서는 로직 수행을 최대한 지양하였습니다.
2. Service layer에서는 추상적인 로직 수행의 순서와, 수행되어야 하는 로직의 분기를 담당했습니다.
3. 또한, 로직 수행 중에 계속 변하는 도메인 객체들을 관리하고 db(in-memory)랑 소통하도록 하였습니다.

## 서로 다른 Domain끼리 의존을 최대한 피하자

1. 미션을 수행하면서 가장 신경 썼던 부분 중 하나는 도메인(바운디드 컨텍스트)끼리의 독립성이였습니다.
2. item/purchase 가 서로 독립적으로 로직을 수행하고 직접적으로 도메인 객체에 접근하는 것을 막았습니다.
3. 서로의 의존성은 ItemStockService를 통해서 db에 저장된 정보를 pull 하기 위하여 사용되었습니다.

## Stateless를 지향하자

1. user와 interruption이 많은 이번 미션 특성 상 여러번 서비스를 호출해야하는 일이 많았습니다.
2. 그럼에도 user - domain logic 간에는 stateless를 보장하려고 노력하였습니다.
3. 각 객체의 'state', 'user에 id할당' 을 통하여 user와 logic간의 stateless를 보장하였습니다. 

## 읽기 쉬운 코드를 작성하자

1. 지난 미션에 대한 공통 피드백을 공부하며 조금 더 읽기 쉬운 코드를 작성하고자 하였습니다.
2. 모든 메서드는 10줄 이내로 유지하려고 했고, 의미가 중복되는 메서드가 있으면 병합하였습니다.
3. 또한, 한 메서드가 명백히 여러 일을 하고 있다면 추출하여 분리했습니다.

---

# 각 Domain의 역할 및 책임

## 1. Item

- 재고 정보를 관리 / 생성 / 수정 / 제공 / 저장 합니다.
- 할인 정책을 생성 / 저장 합니다.

### PromotionRule

- Promotion에 대한 rule 정보를 담고 있는 객체입니다.
- Promotion이 현재 유효한지 검사합니다.
- Promotion할인 수량을 결정합니다.

### Item, PromotionItem

- 저장된 Item에 대한 정보를 저장합니다.
- 개별 Item의 구매(정보수정)로직을 수행합니다.

### ItemInfo

- 통합된 Item에 대한 정보를 제공합니다.
- Item의 구매 수량 분배를 결정합니다. 

## 2. Purchase

- 각 사용자 별 구매 정보를 생성합니다.
- 할인 정책의 적용 여부를 결정합니다.
- 전체 구매 정보를 user에게 제공합니다. -> 영수증을 발행합니다.

### Purchase, PurchaseStatus

- 개별 사용자의 구매 정보를 제공/저장합니다.
- 각 하위 객체들에 접근하여 로직을 수행합니다.
- status를 통하여 구매 로직의 상태를 저장합니다.

### PurchaseItem, PurchaseItems

- 구매하고자 하는 Item의 정보를 저장합니다.

### PromotionDiscount, PromotionDiscountStatus, PromotionDiscountItem

- Promotion 할인을 받을 수 있는 Item의 정보를 갖고 있습니다.
- 추가 할인 여부 / 할인 불가 여부의 정보를 갖고 있습니다.
- status를 통해 사용자의 input에 따라 정보를 처리할 수 있도록 합니다.
- DiscountFactory(service)를 통해 item domain과 통신하여 정보를 가져와 저장합니다.

### MembershipDiscount

- Membership discount 금액을 저장 및 계산합니다.

### Receipt, ReceiptPrice (value object)

- 전체 구매에 대한 정보를 저장합니다.
- 구매 정보를 사용자에게 전달하기 위해 사용됩니다.

## 3. 기타(user)

### PurchaseContextHandler, MainContextController

- 사용자의 입장에서 일정한 flow의 context를 갖고 domain의 controller를 호출합니다.
- client-server의 관점으로 보면 Client에 해당합니다.

### Parser, Converter, Reader

- user, file 의 input을 변환하고 검증하기 위해 사용됩니다.

### I/O Handler

- 사용자의 input을 받고 output을 주는 interface에 해당합니다.
- 입/출력의 entrypoint입니다.

### Formatter

- 출력하기 위한 String 제작에 사용됩니다.

---
## 아쉬운 점, 더 노력할 점
이번 미션을 해결하며 느낀 아쉬운 점에 대한 내용입니다.
1. 완전히 domain간에 의존을 없애지 못했습니다. 차후 mq, event handler를 이용하여 개선할 수 있을 것 같습니다.
2. domain을 복잡하게 구성하였습니다. 최대한 간소하게 설계/구현하려 했으나 더욱 잘게 쪼개고 싶은 마음에 여러 클래스로 나눴습니다. 그 결과 조금은 구조가 더 복잡해진 것 같습니다. 차후 설계시에는 더욱 압축되고 견고하게 설계해야할 것입니다.
3. 메서드의 길이는 길지 않지만, 복잡한 메서드가 존재합니다. 메서드의 역할을 명백히 분리하지 못했고, 이후에는 더욱 잘게 분리하는 연습을 해야할 것입니다.
4. TEST코드를 완벽히 구현하지 못했습니다. service가 복잡해짐에 따라 given을 구현하기 어려워졌습니다. mock 객체를 적절히 활용하여 test를 수행하는 연습을 해야할 것 같습니다.
