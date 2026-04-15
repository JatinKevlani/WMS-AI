# WMS-AI: UML & Use Case Diagrams
## UML_Diagrams.md — v1.0
**Date:** March 28, 2026 | **Supervised by:** CODEX

---

## 1. Diagram Index

| # | Diagram | Type | Coverage |
|---|---------|------|----------|
| D1 | Use Case Diagram | UML Use Case | All actors, system boundary, 12 use cases |
| D2 | Class Hierarchy Diagram | UML Class | Inheritance tree + entity relationships |
| D3 | Activity Flow (text) | UML Activity | Alert trigger flow |
| D4 | State Diagram (text) | UML State | Purchase Order lifecycle |
| D5 | Sequence Diagram (text) | UML Sequence | AI recommendation sequence |

> D1 and D2 are rendered as SVG visuals in the chat.
> D3–D5 are described in PlantUML notation below.

---

## 2. Use Case Diagram Summary

**Actors:** Admin, Staff

**Shared Use Cases (both actors):**
- Login
- Manage products (CRUD, stock in/out)
- Record sales
- View analytics
- View dashboard
- Stock in / out

**Admin-only Use Cases:**
- System settings (configure thresholds, overstock days)
- Manage users (create/deactivate Staff accounts)
- Create purchase orders
- Manage alerts (view, dismiss, act on AI recommendations)
- AI recommendations (trigger + save bundle suggestions)
- Export / import files (CSV)

**Key relationships:**
- "Create purchase orders" `<<include>>` "Manage suppliers" (supplier must exist)
- "Manage alerts" `<<extend>>` "Get AI tips" (optional AI tips on overstock alert)
- "Record sales" `<<include>>` "Stock in / out" (always decrements stock)

---

## 3. Class Hierarchy Summary

### Inheritance Tree
```
«interface» Searchable
     ▲ (implements — dashed arrow)
     │
«abstract» Person
  - id: int
  - name: String
  - email: String
     ▲ (extends — solid arrow)
     │
   Employee
  - employeeId: String
  - role: Role (enum)
  + searchById(int): Employee    ← overrides Aadhaar search with Employee ID
  + searchByName(String): Employee
     ├──────────────────────────┐
     ▲                          ▲
  AdminUser                  StaffUser
  role = ADMIN               role = STAFF

(Supplier also implements Searchable — separate branch)
```

### Key Entity Associations
```
Product ──── N:1 ──── Category
Product ──── N:1 ──── Supplier

PurchaseOrder ──◆── 1..* ──── OrderItem ──── N:1 ──── Product
                (composition)

SalesTransaction ──── N:1 ──── Product

Alert ──── refs ──── Product

AIRecommendationLog ──── refs ──── Product
ProductBundle ──── refs ──── Product
```

---

## 4. Activity Diagram — Alert Trigger Flow (PlantUML)

```plantuml
@startuml
|Stock Operation|
start
:Staff/Admin triggers Stock-In or Stock-Out;

|Alert Service|
:AlertService.checkThresholds(Product);

if (quantity < min_threshold?) then (yes)
  :createAlert(LOW_STOCK, product);
  :EmailService.sendLowStockEmail();
  :Dashboard notification created;
  :Suppressor: skip if alerted in last 24h;
else (no)
  if (quantity > max_threshold AND last_sale > overstockDays?) then (yes)
    :createAlert(OVERSTOCK, product);
    :RecommendationService.getOverstockStrategies();
    :EmailService.sendOverstockEmail(strategies);
    :Dashboard notification created;
  else (no)
    :No alert triggered;
  endif
endif
stop
@enduml
```

---

## 5. State Diagram — Purchase Order Lifecycle (PlantUML)

```plantuml
@startuml
[*] --> PENDING : Admin creates order

PENDING --> SENT : Admin marks as sent to supplier
PENDING --> CANCELLED : Admin cancels

SENT --> RECEIVED : Supplier delivers, Admin marks received
SENT --> CANCELLED : Order cancelled in transit

RECEIVED --> [*] : System auto-increments stock quantities for each OrderItem

CANCELLED --> [*]

note on link
  RECEIVED triggers:
  stockIn(product, qty)
  for each OrderItem
end note
@enduml
```

---

## 6. Sequence Diagram — AI Bundle Recommendation Flow (PlantUML)

```plantuml
@startuml
actor Admin as A
participant "React Frontend" as FE
participant "ProductController" as PC
participant "ProductService" as PS
participant "RecommendationService" as RS
participant "Claude API" as AI
participant "PostgreSQL" as DB

A -> FE: Fill product form, click "Save"
FE -> PC: POST /api/products (ProductDTO, JWT)
PC -> PS: createProduct(dto)
PS -> DB: INSERT product record
DB --> PS: Product saved (id=42)
PS -> RS: getBundleRecommendations(product) [async]

RS -> AI: POST /v1/messages (bundle prompt)
AI --> RS: JSON { suggestions: [...] }

alt AI API available
  RS -> DB: INSERT ai_recommendation_log
  RS --> PS: List<BundleSuggestion>
else API unavailable
  RS -> DB: SELECT co-purchased products (fallback)
  RS --> PS: List<BundleSuggestion> (rule-based)
end

PS --> PC: ProductResponse (with suggestions)
PC --> FE: 201 Created + { product, bundles: [...] }
FE -> A: Show product detail + BundleRecommendationModal

A -> FE: Select 3 suggestions, click "Save selected"
FE -> PC: POST /api/products/42/bundles (selected list)
PC -> DB: INSERT product_bundles (3 records)
DB --> PC: Saved
PC --> FE: 200 OK
FE -> A: "Bundle recommendations saved!"
@enduml
```

---

## 7. OOPJ Diagram Coverage

| OOPJ Requirement | Diagram Type | Where Shown |
|-----------------|-------------|-------------|
| Class, Object, Activity diagrams | Class + Activity | D2, D3 |
| State diagram | State | D4 |
| Interaction diagram (Sequence) | Sequence | D5 |
| Use case diagram | Use Case | D1 |
| Aggregation | Class (OrderItem in Order) | D2 |
| Composition | Class (◆ diamond on Order) | D2 |
| Association | Class (Product–Category, etc.) | D2 |
| Inheritance | Class (Person hierarchy) | D2 |
| Interface | Class (Searchable) | D2 |
| Encapsulation | Class (private fields shown) | D2 |
