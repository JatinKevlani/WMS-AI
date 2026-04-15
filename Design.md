# WMS-AI: Technical Architecture & Design Document
## Design.md — v1.0
**Date:** March 28, 2026 | **Supervised by:** CODEX

---

## 1. System Architecture Overview

```
┌─────────────────────────────────────────────────────────────────────┐
│                          CLIENT LAYER                               │
│              React.js (JSX) + Tailwind CSS                          │
│   Pages: Dashboard | Products | Suppliers | Orders | Analytics      │
│         Alerts | Settings | Login                                    │
└───────────────────────────┬─────────────────────────────────────────┘
                            │ HTTPS / REST JSON
┌───────────────────────────▼─────────────────────────────────────────┐
│                       API GATEWAY LAYER                             │
│              Spring Boot 3.x — Embedded Tomcat                      │
│         JWT Filter → Role Guard → Controller → Service              │
├──────────┬──────────┬──────────┬──────────┬───────────┬─────────────┤
│  Auth    │ Product  │ Supplier │  Order   │  Sales    │ Recommend.  │
│Controller│Controller│Controller│Controller│Controller │ Controller  │
└──────────┴──────────┴──────────┴──────────┴───────────┴──────┬──────┘
                            │                                   │
┌───────────────────────────▼──────────────────────┐  ┌────────▼──────┐
│                   SERVICE LAYER                  │  │  AI API Layer │
│  ProductService | OrderService | AlertService    │  │  Claude API   │
│  SalesService | RecommendationService            │  │  (Anthropic)  │
│  FileExportService | AuthService                 │  └───────────────┘
└───────────────────────────┬──────────────────────┘
                            │
┌───────────────────────────▼──────────────────────┐
│                 REPOSITORY LAYER                  │
│     Spring Data JPA Repositories (JDBC fallback) │
│  ProductRepo | OrderRepo | SaleRepo | AlertRepo   │
└───────────────────────────┬──────────────────────┘
                            │
┌───────────────────────────▼──────────────────────┐
│              INFRASTRUCTURE LAYER                 │
├─────────────────────┬────────────────────────────┤
│   PostgreSQL 15     │   File System               │
│   (Primary DB)      │   /logs, /exports, /backups │
├─────────────────────┼────────────────────────────┤
│   Gmail SMTP        │   Scheduled Jobs (Cron)     │
│   (JavaMail)        │   AlertScheduler (24hr)     │
└─────────────────────┴────────────────────────────┘
```

---

## 2. Tech Stack

### Backend
| Layer | Technology | Version | Reason |
|-------|-----------|---------|--------|
| Language | Java | 17 LTS | LTS, Spring Boot 3 requirement |
| Framework | Spring Boot | 3.2.x | Rapid dev, embedded Tomcat, DI |
| ORM | Spring Data JPA (Hibernate) | 3.2.x | Entity management, repositories |
| DB Driver | PostgreSQL JDBC | 42.x | Official driver |
| Security | Spring Security + JWT | 6.x | Role-based auth |
| JWT Library | JJWT (io.jsonwebtoken) | 0.12.x | Token gen/validation |
| Email | JavaMail (Spring Mail) | 3.x | Gmail SMTP |
| HTTP Client | Spring WebClient | 3.x | Claude API calls |
| Scheduler | Spring @Scheduled | Built-in | Daily alert cron job |
| Logging | SLF4J + Logback | Built-in | File + console logging |
| Build | Maven | 3.9.x | Dependency management |
| Password Hash | BCrypt (Spring Security) | Built-in | Password hashing |

### Frontend
| Layer | Technology | Version | Reason |
|-------|-----------|---------|--------|
| Language | JavaScript (JSX) | ES2022 | Modern JS |
| Framework | React.js | 18.x | Component-based UI |
| Build Tool | Vite | 5.x | Fast dev server |
| Styling | Tailwind CSS | 3.x | Utility-first, fast styling |
| Charts | Recharts | 2.x | React-native charting |
| HTTP Client | Axios | 1.x | API calls with interceptors |
| State | React Context + useState | Built-in | Lightweight state |
| Router | React Router v6 | 6.x | SPA routing |

### Database
| Component | Technology |
|-----------|-----------|
| Primary DB | PostgreSQL 15 |
| Migrations | Flyway (SQL scripts in `/db/migration`) |
| Connection Pool | HikariCP (default Spring Boot) |

### Infrastructure
| Component | Technology |
|-----------|-----------|
| AI API | Anthropic Claude API (claude-sonnet-4-20250514) |
| Email | Gmail SMTP (port 587, TLS) |
| File Storage | Local filesystem (`/wms-data/`) |
| Deployment | Localhost / Any Linux server with JDK 17 |

---

## 3. Project Package Structure

```
wms-ai/
├── backend/                          # Spring Boot Application
│   ├── src/main/java/com/wmsai/
│   │   ├── WmsAiApplication.java     # Main entry point
│   │   ├── config/
│   │   │   ├── SecurityConfig.java   # JWT + Role config
│   │   │   ├── CorsConfig.java       # Allow React origin
│   │   │   ├── MailConfig.java       # Gmail SMTP bean
│   │   │   └── AIConfig.java         # Claude API WebClient
│   │   ├── controller/
│   │   │   ├── AuthController.java
│   │   │   ├── ProductController.java
│   │   │   ├── SupplierController.java
│   │   │   ├── OrderController.java
│   │   │   ├── SalesController.java
│   │   │   ├── AlertController.java
│   │   │   ├── RecommendationController.java
│   │   │   └── FileController.java
│   │   ├── service/
│   │   │   ├── AuthService.java
│   │   │   ├── ProductService.java
│   │   │   ├── SupplierService.java
│   │   │   ├── OrderService.java
│   │   │   ├── SalesService.java
│   │   │   ├── AlertService.java
│   │   │   ├── RecommendationService.java  # Hybrid engine
│   │   │   ├── EmailService.java
│   │   │   └── FileExportService.java
│   │   ├── repository/
│   │   │   ├── UserRepository.java
│   │   │   ├── ProductRepository.java
│   │   │   ├── SupplierRepository.java
│   │   │   ├── OrderRepository.java
│   │   │   ├── SalesRepository.java
│   │   │   └── AlertRepository.java
│   │   ├── model/                    # JPA Entities
│   │   │   ├── User.java
│   │   │   ├── Product.java
│   │   │   ├── Category.java
│   │   │   ├── Supplier.java
│   │   │   ├── PurchaseOrder.java
│   │   │   ├── OrderItem.java
│   │   │   ├── SalesTransaction.java
│   │   │   ├── Alert.java
│   │   │   ├── ProductBundle.java
│   │   │   └── AIRecommendationLog.java
│   │   ├── dto/                      # Request/Response DTOs
│   │   │   ├── LoginRequest.java
│   │   │   ├── LoginResponse.java
│   │   │   ├── ProductDTO.java
│   │   │   ├── StockUpdateDTO.java
│   │   │   ├── SalesDTO.java
│   │   │   └── AnalyticsDTO.java
│   │   ├── exceptions/               # OOPJ Exception Coverage
│   │   │   ├── InsufficientStockException.java  # Checked
│   │   │   ├── DuplicateSKUException.java       # Unchecked
│   │   │   ├── ProductNotFoundException.java    # Unchecked
│   │   │   ├── InvalidSKUException.java         # Unchecked
│   │   │   └── GlobalExceptionHandler.java      # @ControllerAdvice
│   │   ├── interfaces/
│   │   │   └── Searchable.java       # OOPJ Interface requirement
│   │   ├── scheduler/
│   │   │   └── AlertScheduler.java   # Daily overstock check cron
│   │   ├── security/
│   │   │   ├── JwtUtil.java
│   │   │   ├── JwtFilter.java
│   │   │   └── UserDetailsServiceImpl.java
│   │   └── util/                     # OOPJ Utility Classes
│   │       ├── MathUtils.java            # Sum with/without params
│   │       ├── StringUtils.java          # String length 2 ways
│   │       ├── SKUValidator.java         # Palindrome, SKU rules
│   │       ├── BarcodeUtils.java         # bin2Dec
│   │       ├── TextAnalyzer.java         # Vowel/consonant/capital
│   │       ├── VowelTracker.java         # Running vowel total
│   │       ├── ProductCounter.java       # Static var + method
│   │       ├── SalesAverageCalculator.java  # Avg of 4+ subjects
│   │       ├── RecursiveUtils.java       # Recursive smallest int
│   │       ├── TagDeduplicator.java      # Non-duplicate words
│   │       ├── NotificationQueue.java    # ArrayList mixed objects
│   │       ├── WarehouseZone.java        # Constructor overloading
│   │       ├── OrderQueue.java           # PriorityQueue + Cloneable
│   │       └── InventoryLogWriter.java   # File I/O + 150 integers
│   ├── src/main/resources/
│   │   ├── application.properties    # DB, mail, JWT config
│   │   └── db/migration/
│   │       ├── V1__create_tables.sql
│   │       ├── V2__seed_categories.sql
│   │       └── V3__seed_admin_user.sql
│   └── pom.xml
│
├── frontend/                         # React Application
│   ├── src/
│   │   ├── App.jsx
│   │   ├── main.jsx
│   │   ├── api/
│   │   │   └── axios.js              # Axios instance + JWT interceptor
│   │   ├── context/
│   │   │   └── AuthContext.jsx       # Auth state, login/logout
│   │   ├── pages/
│   │   │   ├── LoginPage.jsx
│   │   │   ├── DashboardPage.jsx
│   │   │   ├── ProductsPage.jsx
│   │   │   ├── ProductDetailPage.jsx
│   │   │   ├── SuppliersPage.jsx
│   │   │   ├── OrdersPage.jsx
│   │   │   ├── SalesPage.jsx
│   │   │   ├── AnalyticsPage.jsx
│   │   │   ├── AlertsPage.jsx
│   │   │   └── SettingsPage.jsx
│   │   ├── components/
│   │   │   ├── layout/
│   │   │   │   ├── Sidebar.jsx
│   │   │   │   ├── Navbar.jsx
│   │   │   │   └── PrivateRoute.jsx
│   │   │   ├── dashboard/
│   │   │   │   ├── StatsCard.jsx
│   │   │   │   ├── SalesChart.jsx
│   │   │   │   ├── TopProductsWidget.jsx
│   │   │   │   └── AlertsBadge.jsx
│   │   │   ├── products/
│   │   │   │   ├── ProductTable.jsx
│   │   │   │   ├── ProductForm.jsx
│   │   │   │   ├── StockUpdateModal.jsx
│   │   │   │   └── BundleRecommendationModal.jsx
│   │   │   ├── alerts/
│   │   │   │   └── AlertList.jsx
│   │   │   └── common/
│   │   │       ├── Modal.jsx
│   │   │       ├── Table.jsx
│   │   │       ├── Pagination.jsx
│   │   │       └── SearchBar.jsx
│   └── package.json
│
└── demos/                            # Standalone OOPJ C demos
    ├── SumDemo.c
    └── SumNoParam.c
```

---

## 4. Core Class Design

### 4.1 Inheritance & Interface Hierarchy (OOPJ Core)

```
«interface»
Searchable
+ searchById(int id): Object
+ searchByName(String name): Object
        ▲
        │ implements
«abstract»
Person
- id: int
- name: String
- email: String
# Person(String name, String email)
+ getName(): String
+ getEmail(): String
        ▲
        │ extends
Employee
- employeeId: String
- role: Role
+ searchById(int id): Employee   ← overrides Aadhaar with Employee ID
+ searchByName(String name): Employee
        ▲
        │ extends
AdminUser          StaffUser
```

### 4.2 WarehouseZone — Constructor Overloading

```java
// Cube zone  → Zone(double a)              Volume = a³
// Cuboid zone → Zone(double l, double b, double h) Volume = l×b×h
// Cylinder    → Zone(double r, double h)   Volume = π×r²×h
// Copy        → Zone(WarehouseZone other)  Copy constructor
```

### 4.3 OrderQueue — PriorityQueue + Cloneable

```java
public class OrderQueue extends PriorityQueue<PurchaseOrder>
                        implements Cloneable {
    @Override
    public OrderQueue clone() { ... }
}
```

### 4.4 RecommendationService — Hybrid Engine Logic

```
Input: Product (name, category, sales_history)
  │
  ├─► Rule Engine
  │     - If category matches known bundle map → return rule recs
  │     - If product unsold > overstock_days → generate velocity rec
  │
  ├─► AI API Engine (Claude)
  │     - Build prompt with product context
  │     - Call /v1/messages
  │     - Parse JSON response
  │     - Log to ai_recommendation_log
  │
  └─► Fallback (if API unreachable)
        - Return top co-purchased products from sales_transactions
```

### 4.5 AlertService — Alert Trigger Flow

```
StockUpdateEvent fired
        │
        ▼
AlertService.checkThresholds(Product p)
        │
        ├─ quantity < min_threshold?
        │       └─ YES → createAlert(LOW_STOCK)
        │                → EmailService.sendLowStockEmail()
        │
        └─ quantity > max_threshold AND
           last_sale_date < now - overstock_days?
                └─ YES → createAlert(OVERSTOCK)
                         → RecommendationService.getOverstockTips()
                         → EmailService.sendOverstockEmail(tips)
```

---

## 5. Database Schema (Entity Relationship)

```
users ──────────────────────────────────────────────────────────────────────┐
  │ id, email, password_hash, role                                           │
  │                                                                          │
  │ created_by                                                               │
  ▼                                                                          │
purchase_orders ─────────────────── order_items ──────────────────────┐     │
  │ id, supplier_id, status          │ id, order_id, product_id, qty   │     │
  │                                  └──────────────────────────┐      │     │
  │                                                             │      │     │
suppliers ──────────────────────────┐                    products ────┘      │
  │ id, name, contact, email        │                      │ id, sku, name   │
  └────────────────────────────────►│◄─ supplier_id        │ category_id     │
                                                           │ min/max thresh  │
categories ─────────────────────────────────────────────►category_id        │
  │ id, name                                              │                  │
                                                          │                  │
sales_transactions ─────────────────────────────────────►product_id         │
  │ id, product_id, qty, price       sold_by ────────────────────────────────┘
  
alerts ─────────────────────────────────────────────────►product_id
product_bundles ─────────────────────────────────────────►product_id
ai_recommendation_log ───────────────────────────────────►product_id
```

---

## 6. Security Design

```
Request
  │
  ▼
JwtFilter
  │ Extract Bearer token
  │ Validate signature + expiry
  │ Load UserDetails
  │ Set SecurityContext
  ▼
Spring Security Role Guards
  │ @PreAuthorize("hasRole('ADMIN')")
  │ @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
  ▼
Controller
```

**JWT Payload:**
```json
{
  "sub": "user@email.com",
  "role": "ADMIN",
  "userId": "uuid-here",
  "iat": 1711123456,
  "exp": 1711152256
}
```

---

## 7. Recommendation Engine — AI Prompt Templates

### 7.1 New Product Bundle Prompt
```
System: You are a warehouse inventory consultant. Respond ONLY in JSON.
User: A warehouse has added a new product: "{productName}" in category 
"{category}". Suggest 5 complementary products to stock alongside it 
to maximize cross-selling. Respond as:
{"suggestions": [{"name": "...", "reason": "..."}]}
```

### 7.2 Overstock Resolution Prompt
```
System: You are a retail sales strategist. Respond ONLY in JSON.
User: Product "{productName}" (category: "{category}") has {quantity} units 
in stock and hasn't sold in {days} days. Suggest 4 actionable strategies 
to move this inventory. Respond as:
{"strategies": [{"title": "...", "description": "..."}]}
```

---

## 8. Email Templates

### 8.1 Low Stock Alert
```
Subject: [WMS-AI Alert] Low Stock: {productName}
Body:
  Product "{productName}" (SKU: {sku}) has dropped to {quantity} units,
  below the minimum threshold of {minThreshold}.
  Suggested reorder quantity: {reorderQty} units.
  Action: Login to WMS-AI → Orders → Create Purchase Order
```

### 8.2 Overstock Alert
```
Subject: [WMS-AI Alert] Overstock Detected: {productName}
Body:
  Product "{productName}" has {quantity} units with no sales in {days} days.
  AI Recommendations to move inventory:
  1. {strategy1}
  2. {strategy2}
  ...
```

---

## 9. File I/O Design

| File | Location | Format | When Written |
|------|----------|--------|--------------|
| System log | `/wms-data/logs/wms_system.log` | Append text | Every request/event |
| Inventory backup | `/wms-data/backups/inventory_YYYY-MM-DD.txt` | Text | Daily at midnight |
| Sales export | `/wms-data/exports/sales_report.csv` | CSV | On-demand |
| Inventory export | `/wms-data/exports/inventory.csv` | CSV | On-demand |
| Product import | User upload → temp file | CSV | On bulk import |
| Random int log | `/wms-data/logs/stock_sample.txt` | Space-separated | Demo / on demand |

---

## 10. Frontend Page Wireframe Summary

| Page | Key Components |
|------|---------------|
| Login | Email + Password form, JWT stored in localStorage |
| Dashboard | 4 stat cards, sales chart (Recharts), top products, alert badge |
| Products | Searchable table, Add/Edit modal, Stock-in/out modal, Bundle modal |
| Suppliers | Table with linked products count, Add/Edit supplier form |
| Orders | Order list with status filter, Create order form with line items |
| Sales | Transaction table with date filter, Record sale form |
| Analytics | Period selector (MoM/QoQ/YoY/Custom), comparison bar chart, category breakdown |
| Alerts | List of unread alerts with dismiss action, AI tips accordion |
| Settings | Global threshold config, overstock days, user management (Admin only) |
