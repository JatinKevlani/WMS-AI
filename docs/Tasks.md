# WMS-AI: Engineering Task Breakdown
## Tasks.md — v1.0
**Date:** March 28, 2026 | **Deadline:** March 30, 2026 | **Supervised by:** CODEX

> All tasks are atomic — each task is completable in 1–4 hours.
> Tasks are grouped by Phase and Module.
> OOPJ tasks are tagged with [OOPJ-#] matching the PRD coverage matrix.

---

## PHASE 1 — Foundation & Core Backend (Day 1: March 28)

### P1-SETUP: Project Scaffolding
- [ ] **T001** — Init Spring Boot project via Spring Initializr with deps: Web, JPA, PostgreSQL, Security, Mail, Validation, Lombok
- [ ] **T002** — Init React project with Vite; install: Axios, React Router v6, Tailwind CSS, Recharts
- [ ] **T003** — Create PostgreSQL database `wms_ai_db`; configure `application.properties` (DB URL, credentials, JWT secret, mail config)
- [ ] **T004** — Add Flyway dependency; write `V1__create_tables.sql` for all 11 tables from PRD data models
- [ ] **T005** — Write `V2__seed_categories.sql` (10 sample categories)
- [ ] **T006** — Write `V3__seed_admin_user.sql` (default admin: admin@wms.com / admin123)
- [ ] **T007** — Create `/wms-data/logs`, `/wms-data/exports`, `/wms-data/backups` directories; add to `.gitignore`

### P1-AUTH: Authentication Module
- [ ] **T008** — Create `User.java` JPA entity with fields from PRD schema; `Role` enum (ADMIN, STAFF)
- [ ] **T009** — Create `UserRepository.java` extending JpaRepository; add `findByEmail()` method
- [ ] **T010** — Create `JwtUtil.java`: `generateToken(UserDetails)`, `validateToken(String)`, `extractUsername(String)` [OOPJ-18 encapsulation]
- [ ] **T011** — Create `JwtFilter.java` extending `OncePerRequestFilter`; extract + validate JWT on each request
- [ ] **T012** — Create `SecurityConfig.java`: configure BCrypt bean, CORS, stateless session, public paths (`/api/auth/**`)
- [ ] **T013** — Create `AuthService.java`: `login(LoginRequest)`, `register(RegisterRequest)` with BCrypt hashing
- [ ] **T014** — Create `AuthController.java`: POST `/api/auth/login`, POST `/api/auth/register`
- [ ] **T015** — Test auth with Postman: login returns JWT, protected endpoint returns 403 without token

### P1-MODELS: Core JPA Entities
- [ ] **T016** — Create `Category.java` JPA entity + `CategoryRepository.java`
- [ ] **T017** — Create `Supplier.java` JPA entity; implement `Searchable` interface [OOPJ-19]
- [ ] **T018** — Create `Product.java` JPA entity with all fields from PRD schema; `@ManyToOne` to Category, Supplier
- [ ] **T019** — Create `PurchaseOrder.java` + `OrderItem.java` JPA entities; `@OneToMany` relationship [OOPJ-24]
- [ ] **T020** — Create `SalesTransaction.java` JPA entity; `@ManyToOne` to Product, User
- [ ] **T021** — Create `Alert.java` JPA entity; `AlertType` enum (LOW_STOCK, OVERSTOCK, INFO)
- [ ] **T022** — Create `ProductBundle.java` + `AIRecommendationLog.java` JPA entities
- [ ] **T023** — Create all Repository interfaces (ProductRepo, SupplierRepo, OrderRepo, SalesRepo, AlertRepo)

### P1-EXCEPTIONS: Exception Handling [OOPJ-22, 23]
- [ ] **T024** — Create `InsufficientStockException.java` (checked exception — extends Exception)
- [ ] **T025** — Create `DuplicateSKUException.java`, `ProductNotFoundException.java`, `InvalidSKUException.java` (unchecked — extends RuntimeException)
- [ ] **T026** — Create `GlobalExceptionHandler.java` with `@ControllerAdvice`; map each exception to HTTP status + JSON error response

---

## PHASE 2 — Core WMS Features (Day 1–2: March 28–29)

### P2-PRODUCT: Product & Inventory Module
- [ ] **T027** — Create `ProductService.java`:
  - `getAllProducts(Pageable)` 
  - `getProductById(int id)` — throws ProductNotFoundException
  - `createProduct(ProductDTO)` — validates SKU uniqueness (throws DuplicateSKUException), calls ProductCounter.increment() [OOPJ-15]
  - `updateProduct(int id, ProductDTO)`
  - `softDeleteProduct(int id)` — ADMIN only
- [ ] **T028** — Create `ProductSearchService.java` with overloaded search methods [OOPJ-7]:
  - `search(String name)`
  - `search(int id)`
  - `search(String name, String category)`
- [ ] **T029** — Create `StockService.java`:
  - `stockIn(int productId, int qty)` — logs transaction, fires alert check
  - `stockOut(int productId, int qty)` — validates qty, throws InsufficientStockException if insufficient [OOPJ-22]
- [ ] **T030** — Create `ProductController.java` with all endpoints from PRD Section 9
- [ ] **T031** — Add `SKUValidator.java`: `isValidSKU(String)`, `isPalindrome(String)` [OOPJ-17]
- [ ] **T032** — Add `ProductCounter.java` with static `totalCount` field and static `increment()`, `getCount()` methods [OOPJ-15]
- [ ] **T033** — Add `StringUtils.java`: `lengthUsingLength(String)`, `lengthUsingToCharArray(String)` [OOPJ-10]

### P2-SUPPLIER: Supplier Module [OOPJ-19, 20, 21]
- [ ] **T034** — Create `Searchable.java` interface with `searchById(int)` and `searchByName(String)` methods
- [ ] **T035** — Create `Person.java` abstract class; `Employee.java` extending Person implementing Searchable [OOPJ-18, 20]
- [ ] **T036** — Make `Supplier.java` implement `Searchable`; implement both search methods
- [ ] **T037** — Create `SupplierService.java`: CRUD + search; maintains `List<Supplier>` in service layer, returns full object on search [OOPJ-21]
- [ ] **T038** — Create `SupplierController.java` with GET/POST/PUT endpoints

### P2-ORDERS: Purchase Order Module [OOPJ-24, 28]
- [ ] **T039** — Create `OrderQueue.java` extending `PriorityQueue<PurchaseOrder>` implementing `Cloneable` [OOPJ-28]:
  - `clone()` method deep copies the queue
  - `compareTo` orders by PENDING first
- [ ] **T040** — Create `OrderService.java`:
  - `createOrder(OrderDTO)` — creates order + line items
  - `updateOrderStatus(int id, OrderStatus)` — on RECEIVED, calls stockIn for each line item
  - `getOrdersBySupplier(int supplierId)`
  - `getOrdersByStatus(OrderStatus)`
- [ ] **T041** — Create `OrderController.java` with all order endpoints
- [ ] **T042** — Create `NotificationQueue.java` — ArrayList holding `Alert`, `LocalDate`, `String`, `WarehouseZone` objects [OOPJ-25]

### P2-SALES: Sales Transaction Module
- [ ] **T043** — Create `SalesService.java`:
  - `recordSale(SalesDTO)` — calls stockOut, logs to DB, appends to log file [FILE-03]
  - `getTransactions(LocalDate from, LocalDate to, Integer productId)`
  - `getTotalRevenue(LocalDate from, LocalDate to)`
- [ ] **T044** — Create `SalesController.java` with GET/POST endpoints
- [ ] **T045** — Implement `SalesAverageCalculator.java` — calculates avg sales for 4+ product categories using 2 calculator objects [OOPJ-6]; used in analytics API

### P2-ALERTS: Alert & Notification Module
- [ ] **T046** — Create `AlertService.java`:
  - `checkThresholds(Product)` — checks low-stock and overstock conditions
  - `createAlert(AlertType, Product, String message)`
  - `getUnreadAlerts()`
  - `markAsRead(int alertId)`
  - `isDuplicateAlert(int productId, AlertType, LocalDateTime within24hrs)` — suppresses duplicate alerts
- [ ] **T047** — Create `AlertController.java`: GET `/api/alerts`, PUT `/api/alerts/{id}/read`, GET `/api/alerts/count`
- [ ] **T048** — Create `AlertScheduler.java` with `@Scheduled(cron = "0 0 0 * * *")` — daily overstock check across all products
- [ ] **T049** — Create `EmailService.java` using JavaMailSender:
  - `sendLowStockEmail(Product p, int reorderQty)`
  - `sendOverstockEmail(Product p, List<String> strategies)`
  - Configure Gmail SMTP in `application.properties`

---

## PHASE 3 — Recommendation Engine + Analytics + File I/O (Day 2: March 29)

### P3-RECOMMEND: Hybrid Recommendation Engine
- [ ] **T050** — Create `AIConfig.java` — configure WebClient bean with Claude API base URL + API key header
- [ ] **T051** — Create `RecommendationService.java`:
  - `getBundleRecommendations(Product)` — calls Claude API with bundle prompt template
  - `getOverstockStrategies(Product)` — calls Claude API with overstock prompt
  - `getRuleBasedBundles(Product)` — fallback: query sales_transactions for co-purchased products
  - `logRecommendation(int productId, String prompt, String response)` — saves to ai_recommendation_log
- [ ] **T052** — Create `RecommendationController.java`:
  - POST `/api/recommendations/bundle`
  - POST `/api/recommendations/overstock`
- [ ] **T053** — Wire `RecommendationService` into `ProductService.createProduct()` — triggers async bundle suggestion after product creation
- [ ] **T054** — Add bundle save endpoints: GET/POST `/api/products/{id}/bundles`

### P3-ANALYTICS: Analytics Module
- [ ] **T055** — Add custom query methods to `SalesRepository.java`:
  - `findRevenueByPeriod(LocalDate from, LocalDate to)`
  - `findTopSellingProducts(LocalDate from, LocalDate to, int limit)`
  - `findSlowMovingProducts(int days, int limit)`
  - `findRevenueByCategory(LocalDate from, LocalDate to)`
- [ ] **T056** — Create `AnalyticsService.java`:
  - `getMoMComparison()` — this month vs last month
  - `getQoQComparison()` — this quarter vs last quarter
  - `getYoYComparison()` — this year vs last year
  - `getCustomComparison(LocalDate from, LocalDate to)`
  - `getDashboardStats()` — total products, inventory value, active alerts, monthly revenue
- [ ] **T057** — Create `AnalyticsController.java`: GET `/api/sales/analytics?period=MOM|QOQ|YOY|CUSTOM&from=&to=`

### P3-FILEIO: File I/O Module [OOPJ-27, 28, 29]
- [ ] **T058** — Create `FileExportService.java`:
  - `exportInventoryToCSV()` — writes all products to `/wms-data/exports/inventory.csv`
  - `exportSalesReportToCSV(LocalDate from, LocalDate to)` — writes sales to CSV
  - `exportSalesReportToTxt(LocalDate from, LocalDate to)` — writes to .txt file
- [ ] **T059** — Create `FileImportService.java`:
  - `importProductsFromCSV(MultipartFile file)` — reads CSV, creates products, returns import summary
- [ ] **T060** — Create `InventoryLogWriter.java`:
  - `writeRandomStockLog()` — writes 150 random integers separated by space to `stock_sample.txt` [OOPJ-27]
  - Uses `RecursiveUtils.findSmallest(int[], int)` to find minimum [OOPJ-27]
- [ ] **T061** — Create `RecursiveUtils.java` with `findSmallest(int[] arr, int index)` recursive method [OOPJ-27]
- [ ] **T062** — Create `TagDeduplicator.java`:
  - `readAndDeduplicate(String filePath)` — reads words from file, returns unique words sorted descending [OOPJ-29]
  - Integrated into product tag management
- [ ] **T063** — Create `FileController.java`:
  - GET `/api/export/inventory` → triggers CSV export, returns file download
  - GET `/api/export/sales` → triggers sales CSV export
  - POST `/api/import/products` → accepts multipart CSV upload
- [ ] **T064** — Configure Logback in `application.properties` to write logs to `/wms-data/logs/wms_system.log`
- [ ] **T065** — Create `BackupScheduler.java` with `@Scheduled(cron = "0 0 0 * * *")` — writes daily inventory backup to txt file [FILE-05]

### P3-OOPJ-UTILS: Remaining OOPJ Utility Classes
- [ ] **T066** — Create `MathUtils.java`: `sumWithParams(int a, int b)`, `sumWithoutParams()` using instance fields [OOPJ-3, 4]
- [ ] **T067** — Create `TextAnalyzer.java`: `countVowelsAndConsonants(String)`, `countCapitalStartWords(String)` [OOPJ-13, 14]
- [ ] **T068** — Create `VowelTracker.java`: loops processing sentences, tracks running count per vowel type, exits on "quit" [OOPJ-16]
- [ ] **T069** — Create `BarcodeUtils.java`: `bin2Dec(String binaryString)` — throws `NumberFormatException` if not binary string [OOPJ-26]
- [ ] **T070** — Create `WarehouseZone.java` with 4 constructors: `Zone(a)`, `Zone(l,b,h)`, `Zone(r,h)`, `Zone(WarehouseZone)` [OOPJ-11, 12]
- [ ] **T071** — Create demo C files: `SumDemo.c` and `SumNoParam.c` in `/demos` folder [OOPJ-1, 2]

---

## PHASE 4 — React Frontend (Day 2: March 29)

### P4-FRONTEND-SETUP
- [ ] **T072** — Configure Tailwind CSS, set up `App.jsx` with React Router v6 routes
- [ ] **T073** — Create `axios.js` API instance with baseURL + JWT Authorization header interceptor
- [ ] **T074** — Create `AuthContext.jsx` — login/logout state, store JWT in localStorage, redirect logic
- [ ] **T075** — Create `PrivateRoute.jsx` — redirect to login if no JWT; `AdminRoute.jsx` — redirect if not ADMIN
- [ ] **T076** — Create `Sidebar.jsx` and `Navbar.jsx` layout components with alert badge

### P4-PAGES
- [ ] **T077** — **LoginPage.jsx** — email/password form, call POST `/api/auth/login`, store JWT
- [ ] **T078** — **DashboardPage.jsx** — fetch stats, render 4 StatsCards, SalesChart, TopProductsWidget, AlertsBadge
- [ ] **T079** — **ProductsPage.jsx** — table with search, pagination, Add button; Stock-in/out buttons per row
- [ ] **T080** — **ProductDetailPage.jsx** — full product details + bundle recommendations display + save
- [ ] **T081** — **SuppliersPage.jsx** — supplier table, add/edit form
- [ ] **T082** — **OrdersPage.jsx** — orders table with status filter, create order form with dynamic line items
- [ ] **T083** — **SalesPage.jsx** — transaction log table with date range filter, record sale form
- [ ] **T084** — **AnalyticsPage.jsx** — period selector buttons, comparison bar chart (Recharts), category pie chart, top/slow products
- [ ] **T085** — **AlertsPage.jsx** — unread alerts list, dismiss button, AI tips accordion for overstock alerts
- [ ] **T086** — **SettingsPage.jsx** (Admin only) — global threshold, overstock days config, user management table

### P4-COMPONENTS
- [ ] **T087** — `Modal.jsx` — reusable modal wrapper
- [ ] **T088** — `Table.jsx` — reusable sortable table with pagination
- [ ] **T089** — `BundleRecommendationModal.jsx` — shows AI bundle suggestions, checkboxes to select + save
- [ ] **T090** — `StockUpdateModal.jsx` — stock-in/out form with quantity input
- [ ] **T091** — `SalesChart.jsx` — Recharts BarChart showing current vs previous period revenue

---

## PHASE 5 — Integration, Testing & Documentation (Day 3: March 30)

### P5-INTEGRATION
- [ ] **T092** — End-to-end test: Login → Add Product → AI Bundle Modal → Save Bundle → View on Product Detail
- [ ] **T093** — End-to-end test: Stock-Out below threshold → Dashboard alert badge appears → Email received
- [ ] **T094** — End-to-end test: Daily scheduler simulation → Overstock detected → AI tips email sent
- [ ] **T095** — End-to-end test: Analytics page — switch between MoM/QoQ/YoY/Custom, verify chart updates
- [ ] **T096** — End-to-end test: Export inventory CSV → download works; Import products CSV → products appear
- [ ] **T097** — Test ADMIN vs STAFF role: Staff cannot access Settings, cannot delete products

### P5-OOPJ-VERIFICATION
- [ ] **T098** — Run all OOPJ utility classes in a `DemoRunner.java` (implements `CommandLineRunner`) on startup — logs output confirming each practical works
- [ ] **T099** — Verify OOPJ coverage matrix: tick off all 30 items in PRD Section 11

### P5-DOCS
- [ ] **T100** — Write `README.md`: setup guide (DB setup, env vars, run backend, run frontend)
- [ ] **T101** — Write `application.properties.example` with all required keys (no real secrets)
- [ ] **T102** — Add JavaDoc to all public service methods
- [ ] **T103** — Write `OOPJ_COVERAGE.md` — one-paragraph explanation of how each practical is implemented

---

## Task Summary

| Phase | Tasks | Estimated Hours |
|-------|-------|----------------|
| Phase 1 — Foundation | T001–T026 | ~8 hours |
| Phase 2 — Core WMS | T027–T049 | ~10 hours |
| Phase 3 — AI + Analytics + Files | T050–T071 | ~8 hours |
| Phase 4 — React Frontend | T072–T091 | ~10 hours |
| Phase 5 — Integration + Docs | T092–T103 | ~4 hours |
| **Total** | **103 tasks** | **~40 hours** |

> Given the 2-day deadline, prioritize Phase 1 → 2 → 3 back-end first, then Phase 4 front-end.
> Phase 5 tasks can be trimmed to T092–T097, T100, T101 if time is short.
