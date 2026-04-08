# WMS-AI: Warehouse Management System with Intelligent Recommendations
## Product Requirement Document (PRD) — v1.0
**Prepared by:** Senior AI Systems Architect  
**Supervised by:** CODEX  
**Date:** March 28, 2026  
**Deadline:** March 30, 2026  
**Status:** FINAL — Ready for Engineering

---

## 1. Abstract

WMS-AI is a full-stack, web-based Warehouse Management System built on Java Spring Boot with a React.js frontend and a PostgreSQL database. Beyond standard inventory management, WMS-AI incorporates a **Hybrid Recommendation Engine** (rule-based logic + Claude AI API) that proactively notifies managers about low stock, overstocking, and product bundling opportunities. The system delivers real-time analytics with MoM, QoQ, YoY, and custom date range comparisons, all surfaced through a clean dashboard. It is designed as an academic demonstration project that **fully covers every OOPJ practical concept** from the course list by mapping each concept to a real, functional WMS feature.

---

## 2. Problem Statement

Warehouse managers today face three recurring pain points:
1. **Reactive restocking** — they discover stockouts only after they happen.
2. **Silent overstocking** — slow-moving inventory ties up capital with no alerts.
3. **Missed bundling revenue** — complementary products are never surfaced when new SKUs are added.

WMS-AI solves all three through automated threshold monitoring, AI-powered suggestions, and proactive email + dashboard notifications.

---

## 3. Vision & Goals

| Goal | Description |
|------|-------------|
| G1 | Full inventory lifecycle management (add, update, delete, transfer, search) |
| G2 | Smart alerting — low stock and overstock notifications via email + dashboard |
| G3 | AI-powered recommendations on new product addition and overstock resolution |
| G4 | Multi-period analytics dashboard with comparison views |
| G5 | Complete OOPJ concept coverage — every practical maps to a real feature |
| G6 | Role-based access — Admin and Staff with distinct permissions |
| G7 | File I/O — export reports, import inventory, maintain system logs |

---

## 4. Success Metrics

| Metric | Target |
|--------|--------|
| All OOPJ practicals covered | 100% (mapped in Section 11) |
| Core WMS features functional | Add, Edit, Delete, Search, Stock-in, Stock-out |
| Alert accuracy | Low-stock alert fires within 1 request cycle of threshold breach |
| Email delivery | Gmail SMTP sends within 30 seconds of trigger |
| Analytics load time | Dashboard renders in < 2 seconds on localhost |
| AI recommendation | Suggestions returned within 5 seconds of new product creation |
| Auth | Admin and Staff login with JWT session management |

---

## 5. User Personas

### 5.1 Admin
- **Full access** to all modules
- Can create/delete users (Staff accounts)
- Can configure global thresholds, overstock days
- Receives all alert emails
- Can approve or dismiss recommendations

### 5.2 Staff
- Can view inventory, add/edit products and stock levels
- Can record sales transactions
- Can view dashboard and analytics (read-only)
- Cannot configure system settings or manage users
- Does NOT receive alert emails (Admin only)

---

## 6. System Scope

### In Scope
- Product & Category management
- Supplier management
- Purchase Order management (create, track, receive)
- Sales Transaction logging
- Low-stock and overstock alert engine
- AI bundle recommendations on new product addition
- AI sales improvement suggestions on overstock detection
- Analytics: MoM, QoQ, YoY, Custom date range
- Gmail SMTP email notifications
- File I/O: report export (.csv/.txt), system logs, product import from file, inventory backup
- JWT-based Auth with Admin + Staff roles
- React.js dashboard frontend

### Out of Scope
- Payment processing
- Barcode scanning hardware integration
- Multi-warehouse support
- Mobile app
- Real supplier API integration

---

## 7. Functional Requirements

### 7.1 Authentication Module

| ID | Requirement |
|----|-------------|
| AUTH-01 | Users must log in with email + password |
| AUTH-02 | Passwords stored as bcrypt hashes |
| AUTH-03 | JWT token issued on login, expires in 8 hours |
| AUTH-04 | Role embedded in JWT payload (`ADMIN` / `STAFF`) |
| AUTH-05 | Protected routes enforce role-based access |
| AUTH-06 | Admin can create, deactivate Staff accounts |

---

### 7.2 Product & Inventory Module

| ID | Requirement |
|----|-------------|
| PROD-01 | Admin/Staff can create a product with: name, SKU, category, supplier, unit price, quantity, min_threshold, max_threshold, overstock_days |
| PROD-02 | SKU must be unique; validated on creation |
| PROD-03 | Admin/Staff can update any product field |
| PROD-04 | Admin can delete a product (soft delete) |
| PROD-05 | Search products by: name, SKU, category, supplier |
| PROD-06 | Stock-In operation increases quantity, logs a transaction |
| PROD-07 | Stock-Out operation decreases quantity, validates sufficient stock, logs a transaction |
| PROD-08 | If stock-out quantity > available, throw `InsufficientStockException` (custom checked exception) |
| PROD-09 | On product creation, trigger AI bundle recommendation (see REC-03) |
| PROD-10 | Product list supports pagination and sorting |

---

### 7.3 Category Module

| ID | Requirement |
|----|-------------|
| CAT-01 | Admin can create/update/delete categories |
| CAT-02 | Each product belongs to exactly one category |
| CAT-03 | Category deletion blocked if products are assigned to it |

---

### 7.4 Supplier Module

| ID | Requirement |
|----|-------------|
| SUP-01 | Admin can create suppliers with: name, contact person, email, phone, address |
| SUP-02 | Search supplier by name or ID |
| SUP-03 | View all products linked to a supplier |
| SUP-04 | Supplier entity implements `Searchable` interface with `searchById()` and `searchByName()` methods |

---

### 7.5 Purchase Order Module

| ID | Requirement |
|----|-------------|
| ORD-01 | Admin can create a purchase order for a supplier with line items (product + qty) |
| ORD-02 | Order statuses: `PENDING → SENT → RECEIVED → CANCELLED` |
| ORD-03 | On status = `RECEIVED`, system auto-increments product stock quantities |
| ORD-04 | Search orders by supplier, status, or date range |
| ORD-05 | Orders are stored in an `ArrayList<Order>` in the service layer (covers OOPJ ArrayList requirement) |
| ORD-06 | `OrderQueue` extends `PriorityQueue` and implements `Cloneable` (covers OOPJ PriorityQueue + clone requirement) |

---

### 7.6 Sales Transaction Module

| ID | Requirement |
|----|-------------|
| SALES-01 | Staff/Admin can record a sale: product, quantity sold, sale price, date |
| SALES-02 | Recording a sale triggers a stock-out operation |
| SALES-03 | Transaction log persisted to DB and also appended to a `.txt` log file (File I/O) |
| SALES-04 | View all transactions with filters: date range, product, category |

---

### 7.7 Alert & Notification Module

| ID | Requirement |
|----|-------------|
| ALERT-01 | After every stock-in/stock-out, system checks if product quantity < `min_threshold` |
| ALERT-02 | If below threshold: create dashboard notification + send Gmail SMTP email to Admin |
| ALERT-03 | A scheduled job (every 24 hours) checks for overstock condition: `quantity > max_threshold AND last_sale_date older than overstock_days` |
| ALERT-04 | If overstock detected: create dashboard notification + send email to Admin with AI-generated sales improvement tips |
| ALERT-05 | Dashboard shows unread notification count badge |
| ALERT-06 | Admin can mark notifications as read/dismissed |
| ALERT-07 | Duplicate alerts suppressed — no repeat alert for same product within 24 hours |
| ALERT-08 | `overstock_days` is configurable per-product; global default settable by Admin |

---

### 7.8 Recommendation Engine Module

| ID | Requirement |
|----|-------------|
| REC-01 | **Low-stock recommendation**: When low-stock alert fires, system suggests reorder quantity based on average monthly sales velocity |
| REC-02 | **Overstock recommendation**: When overstock alert fires, AI API (Claude) is called with product details + sales history; response includes promotional ideas, bundle offers, pricing strategy |
| REC-03 | **New product bundle recommendation**: When a new product is added, AI API is called with product name + category; response suggests 3–5 complementary products to stock/sell together |
| REC-04 | Bundle suggestions are shown to user on product creation screen; user can manually save selected ones to product record |
| REC-05 | Saved bundle relationships are stored in `product_bundles` table |
| REC-06 | Rule-based fallback: if AI API is unavailable, system uses category co-occurrence from historical sales data |
| REC-07 | All AI prompts and responses logged to `ai_recommendation_log` table for auditability |

---

### 7.9 Analytics & Reporting Module

| ID | Requirement |
|----|-------------|
| ANA-01 | Dashboard shows: total products, total inventory value, total sales (current period), active alerts count |
| ANA-02 | Sales chart: total revenue per period, configurable by MoM / QoQ / YoY / Custom date range |
| ANA-03 | Per-product analytics: sales volume, revenue, stock turnover rate |
| ANA-04 | Comparison view: current period vs previous equivalent period (e.g. this month vs last month) |
| ANA-05 | Top 5 best-selling products widget |
| ANA-06 | Top 5 slow-moving products widget |
| ANA-07 | Export analytics report to `.csv` file (File I/O) |
| ANA-08 | Category-level sales breakdown |

---

### 7.10 File I/O Module

| ID | Requirement |
|----|-------------|
| FILE-01 | Export inventory snapshot to `.csv` |
| FILE-02 | Export sales report to `.csv` and `.txt` |
| FILE-03 | Import products from a `.csv` file (bulk upload) |
| FILE-04 | System event log written to `wms_system.log` file (appended, not overwritten) |
| FILE-05 | Daily inventory backup written to `backup/inventory_YYYY-MM-DD.txt` |
| FILE-06 | `RecursiveFileUtils` class provides a recursive method to find the smallest integer in a log array (covers OOPJ recursive requirement) |
| FILE-07 | `WordFrequencyReader` reads a text file and outputs non-duplicate words in descending order (covers OOPJ word-dedup requirement) |

---

## 8. Non-Functional Requirements

| Category | Requirement |
|----------|-------------|
| Performance | API responses < 500ms on localhost for standard CRUD |
| Security | Passwords bcrypt-hashed; JWT for session; role guards on all endpoints |
| Maintainability | Clean package structure; every class single-responsibility |
| Code Quality | JavaDoc on all public methods; no raw SQL strings (use JPA/JDBC templates) |
| Portability | Runs on any machine with JDK 17+, Node 18+, PostgreSQL 15+ |
| Scalability | Service layer abstracted behind interfaces — swappable implementations |
| Observability | All exceptions logged to file + console via SLF4J |

---

## 9. REST API Definitions

### Auth
| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | `/api/auth/login` | None | Login, returns JWT |
| POST | `/api/auth/register` | ADMIN | Create Staff account |
| POST | `/api/auth/logout` | Any | Invalidate session |

### Products
| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| GET | `/api/products` | Any | List all products (paginated) |
| GET | `/api/products/{id}` | Any | Get product by ID |
| GET | `/api/products/search?q=` | Any | Search by name/SKU/category |
| POST | `/api/products` | Any | Create product (triggers AI rec) |
| PUT | `/api/products/{id}` | Any | Update product |
| DELETE | `/api/products/{id}` | ADMIN | Soft delete product |
| POST | `/api/products/{id}/stock-in` | Any | Add stock |
| POST | `/api/products/{id}/stock-out` | Any | Remove stock (sale/adjustment) |
| GET | `/api/products/{id}/bundles` | Any | Get saved bundle recommendations |
| POST | `/api/products/{id}/bundles` | Any | Save selected bundle recommendations |

### Suppliers
| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| GET | `/api/suppliers` | Any | List all suppliers |
| GET | `/api/suppliers/{id}` | Any | Get supplier + linked products |
| POST | `/api/suppliers` | ADMIN | Create supplier |
| PUT | `/api/suppliers/{id}` | ADMIN | Update supplier |

### Orders
| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| GET | `/api/orders` | Any | List orders (filterable) |
| POST | `/api/orders` | ADMIN | Create purchase order |
| PUT | `/api/orders/{id}/status` | ADMIN | Update order status |
| GET | `/api/orders/{id}` | Any | Get order details |

### Sales
| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| GET | `/api/sales` | Any | List transactions (filterable) |
| POST | `/api/sales` | Any | Record a sale |
| GET | `/api/sales/analytics` | Any | Get analytics (period param) |

### Alerts
| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| GET | `/api/alerts` | ADMIN | Get all unread alerts |
| PUT | `/api/alerts/{id}/read` | ADMIN | Mark alert as read |
| GET | `/api/alerts/count` | ADMIN | Get unread count for badge |

### Recommendations
| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | `/api/recommendations/bundle` | Any | Get AI bundle suggestions for product |
| POST | `/api/recommendations/overstock` | ADMIN | Get AI overstock resolution tips |

### File I/O
| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| GET | `/api/export/inventory` | ADMIN | Download inventory .csv |
| GET | `/api/export/sales?from=&to=` | ADMIN | Download sales report .csv |
| POST | `/api/import/products` | ADMIN | Upload products .csv |

---

## 10. Data Models

### users
```
id (UUID, PK), email (VARCHAR, UNIQUE), password_hash (VARCHAR),
full_name (VARCHAR), role (ENUM: ADMIN/STAFF), is_active (BOOL),
created_at (TIMESTAMP)
```

### categories
```
id (SERIAL, PK), name (VARCHAR, UNIQUE), description (TEXT),
created_at (TIMESTAMP)
```

### suppliers
```
id (SERIAL, PK), name (VARCHAR), contact_person (VARCHAR),
email (VARCHAR), phone (VARCHAR), address (TEXT), created_at (TIMESTAMP)
```

### products
```
id (SERIAL, PK), sku (VARCHAR, UNIQUE), name (VARCHAR),
category_id (FK→categories), supplier_id (FK→suppliers),
unit_price (DECIMAL), quantity (INT), min_threshold (INT),
max_threshold (INT), overstock_days (INT), is_deleted (BOOL),
last_sale_date (DATE), created_at (TIMESTAMP), updated_at (TIMESTAMP)
```

### product_bundles
```
id (SERIAL, PK), product_id (FK→products),
bundle_product_name (VARCHAR), reason (TEXT), saved_at (TIMESTAMP)
```

### purchase_orders
```
id (SERIAL, PK), supplier_id (FK→suppliers), status (ENUM),
total_amount (DECIMAL), notes (TEXT),
created_by (FK→users), created_at (TIMESTAMP), updated_at (TIMESTAMP)
```

### order_items
```
id (SERIAL, PK), order_id (FK→purchase_orders),
product_id (FK→products), quantity (INT), unit_price (DECIMAL)
```

### sales_transactions
```
id (SERIAL, PK), product_id (FK→products), quantity_sold (INT),
sale_price (DECIMAL), total_amount (DECIMAL),
sold_by (FK→users), sale_date (TIMESTAMP)
```

### alerts
```
id (SERIAL, PK), type (ENUM: LOW_STOCK/OVERSTOCK/INFO),
product_id (FK→products), message (TEXT), is_read (BOOL),
created_at (TIMESTAMP)
```

### ai_recommendation_log
```
id (SERIAL, PK), recommendation_type (VARCHAR),
product_id (FK→products), prompt_sent (TEXT),
response_received (TEXT), created_at (TIMESTAMP)
```

### system_settings
```
key (VARCHAR, PK), value (VARCHAR), updated_by (FK→users),
updated_at (TIMESTAMP)
```

---

## 11. OOPJ Practical Coverage Matrix

| # | Practical Requirement | WMS-AI Implementation | Class/File |
|---|----------------------|----------------------|------------|
| 1 | C Program: sum with params | Demo utility — `SumDemo.c` in `/demos` folder | `SumDemo.c` |
| 2 | C Program: sum without params | Demo utility — `SumNoParam.c` | `SumNoParam.c` |
| 3 | Java: sum with params | `MathUtils.sumWithParams(a, b)` — used in analytics calc | `MathUtils.java` |
| 4 | Java: sum without params | `MathUtils.sumWithoutParams()` — uses instance fields | `MathUtils.java` |
| 5 | Switch-case, objects, CRUD ops | `InventoryController` uses switch-dispatch pattern; all ops use objects | `InventoryService.java` |
| 6 | User input, 4+ subjects avg, 2+ objects | `SalesAverageCalculator` — avg sales across 4+ product categories using 2 calculator objects | `SalesAverageCalculator.java` |
| 7 | Method overloading (2+ programs) | `ProductSearchService.search(String name)`, `search(int id)`, `search(String name, String category)` | `ProductSearchService.java` |
| 8 | Method overloading real-world case 1 | `NotificationService.send(String msg)`, `send(String msg, String email)`, `send(Alert alert)` | `NotificationService.java` |
| 9 | Method overloading real-world case 2 | `ReportExporter.export(List<Product>)`, `export(List<Sale>, DateRange)`, `export(String filename, byte[] data)` | `ReportExporter.java` |
| 10 | Find length of string (2 ways) | `StringUtils.lengthUsingLength(s)` and `StringUtils.lengthUsingToCharArray(s)` — used in SKU validation | `StringUtils.java` |
| 11 | Constructor overloading — Volume (Cube/Cuboid/Cylinder) | `WarehouseZone` — calculates zone storage volume; constructors: `Zone(a)` for cubic, `Zone(l,b,h)` for cuboid, `Zone(r,h)` for cylindrical silo | `WarehouseZone.java` |
| 12 | Copy Constructor | `WarehouseZone(WarehouseZone other)` — copies zone configuration | `WarehouseZone.java` |
| 13 | Vowel/Consonant counter | `TextAnalyzer.countVowelsAndConsonants(String line)` — used in product description analyzer | `TextAnalyzer.java` |
| 14 | Count words starting with capital | `TextAnalyzer.countCapitalStartWords(String line)` — used in report title formatter | `TextAnalyzer.java` |
| 15 | Static variable + static function | `ProductCounter.totalCount` (static), `ProductCounter.increment()` (static) — tracks live product count | `ProductCounter.java` |
| 16 | Vowel count per type, loop until "quit", running total | `VowelTracker` — analyses product descriptions; loops until sentinel; tracks running totals per vowel | `VowelTracker.java` |
| 17 | Palindrome | `SKUValidator.isPalindrome(String sku)` — checks if SKU is palindrome (novelty check for promo SKUs) | `SKUValidator.java` |
| 18 | Polymorphism, Abstraction, Inheritance, Encapsulation | `Person` (abstract) → `Employee` → `AdminUser`/`StaffUser`; all fields private, getters/setters | `Person.java`, `Employee.java` |
| 19 | Interface — Person, search by name + Aadhaar | `Searchable` interface: `searchByName()`, `searchById()` implemented by `Supplier`, `Employee` | `Searchable.java` |
| 20 | Override search by Aadhaar with Employee ID | `Employee.searchById()` overrides with employee ID logic | `Employee.java` |
| 21 | Encapsulated classes, object list, return full object | `SupplierRepository` stores `List<Supplier>`; returns full object on search | `SupplierRepository.java` |
| 22 | Checked vs Unchecked exceptions | `InsufficientStockException` (checked), `InvalidSKUException` (unchecked/RuntimeException) | `exceptions/` package |
| 23 | User-defined exception | `InsufficientStockException`, `DuplicateSKUException`, `ProductNotFoundException` | `exceptions/` package |
| 24 | Course–Student association (List all courses) | `Order`–`OrderItem` association; list all items per order — analogous to Student–Course | `OrderService.java` |
| 25 | ArrayList with Loan, Date, String, Circle | `NotificationQueue` — ArrayList holding mixed `Alert`, `LocalDate`, `String`, `ZoneInfo` objects | `NotificationQueue.java` |
| 26 | bin2Dec with NumberFormatException | `BarcodeUtils.bin2Dec(String binaryString)` — converts binary barcode to decimal product ID | `BarcodeUtils.java` |
| 27 | File: create/append 150 random integers, recursive smallest | `InventoryLogWriter` writes 150 random stock values to file; `RecursiveUtils.findSmallest(int[], int)` | `InventoryLogWriter.java`, `RecursiveUtils.java` |
| 28 | MyPriorityQueue extends PriorityQueue implements Cloneable | `OrderQueue extends PriorityQueue<Order> implements Cloneable` | `OrderQueue.java` |
| 29 | Non-duplicate words descending from file | `TagDeduplicator` reads product-tag file, outputs unique tags sorted descending | `TagDeduplicator.java` |
| 30 | Use case, class, object, activity, state, interaction diagrams | Delivered as part of this PRD package (UML_Diagrams.md + SVG renders) | `UML_Diagrams.md` |

---

## 12. Constraints & Assumptions

1. **PostgreSQL 15+** must be installed and running locally or on a server.
2. **JDK 17+** required (Spring Boot 3.x dependency).
3. **Node 18+** required for React frontend.
4. **Gmail SMTP** requires App Password (2FA must be enabled on the Gmail account used).
5. **Claude AI API** key required for recommendation features; system degrades gracefully to rule-based fallback if key is absent.
6. SKU format: alphanumeric, 4–12 characters, uppercase.
7. All monetary values stored in INR by default (configurable).
8. Academic/demo deployment — no production hardening (rate limiting, HTTPS, etc.) required.
9. Soft-deleted products are excluded from all active queries but retained in transaction history.
10. The C programs (`SumDemo.c`, `SumNoParam.c`) are standalone files in the `/demos` directory and are not part of the Spring Boot application.
