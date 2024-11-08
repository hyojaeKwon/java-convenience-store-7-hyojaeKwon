# java-convenience-store-precourse

# Domain

## 1. Item

### 속성
- **itemId**: 고유 식별자.
- **name**: 상품 이름.
- **price**: 단가.
- **generalStockQuantity**: 프로모션에 할당되지 않은 사용 가능한 재고.
- **promotionStockQuantity**: 프로모션에 할당된 재고.

### 메서드
- **isPurchasable(quantity)**: 충분한 재고가 있는지 확인합니다.
- **deductStock(quantity, isPromotion)**: 적절한 재고 유형에서 차감합니다.

---

## 2. Promotion

### 속성
- **promotionId**: 고유 식별자.
- **item**: 관련된 Item.
- **promotionType**: 프로모션 유형 (예: "N개 구매 시 1개 무료").
- **requiredQuantity**: 자격을 얻기 위한 필수 수량.
- **startDate, endDate**: 프로모션 유효 기간.
- **promotionStockQuantity**: 프로모션에 할당된 재고.

### 메서드
- **isActive(currentDate)**: 현재 프로모션이 유효한지 확인합니다.
- **calculateFreeItems(purchasedQuantity)**: 무료 상품의 수를 결정합니다.
- **informAdditionalPurchase(purchasedQuantity)**: 자격을 얻기 위해 더 많은 상품이 필요한지 안내합니다.

---

## 3. Purchase

### 속성
- **purchaseId**: 고유 식별자.
- **purchaseItems**: `PurchaseItem` 엔티티의 목록.
- **member**: 멤버십 상태를 나타냅니다.
- **totalBuyingPriceAmount**: 할인 전 소계.
- **promotionDiscountAmount**: 총 프로모션 할인액.
- **membershipDiscountAmount**: 총 멤버십 할인액.
- **finalPriceAmount**: 모든 할인이 적용된 후 지불할 금액.

### 메서드
- **calculateTotals()**: 모든 총액과 할인을 계산합니다.
- **generateReceipt()**: 형식화된 영수증을 생성합니다.

---

## 4. PurchaseItem

### 속성
- **item**: 구매한 Item.
- **quantity**: 구매 수량.
- **price**: 할인 전 총 가격.
- **promotionApplied**: 프로모션이 적용되었는지 여부.
- **freeItems**: 받은 무료 상품의 수.

### 메서드
- **calculateLineTotal()**: 해당 상품의 총 가격을 계산합니다.

---

## 값 객체

### Membership

### 속성
- **isMember**: 멤버십 여부.

### 메서드
- **calculateDiscount(amount)**: 최대 한도 내에서 30% 할인을 적용합니다.

## 각 도메인 애그러거트의 역할

- item
    - item 재고를 관리합니다.
    - item 재고 정보를 저공합니다.
- purchase
    - 구매 로직을 제공합니다.
    - 할인 로직을 적용합니다.
    - 영수증을 발행합니다.