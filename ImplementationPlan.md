# WMS-AI: Implementation Plan
## ImplementationPlan.md — v1.0
**Deadline: March 30, 2026 (48 hours from March 28)**

---

## Realistic 48-Hour Execution Plan

> ⚠️ This is an aggressive but achievable plan for a focused developer.
> Priority order: Backend Core → Recommendation Engine → Frontend → Polish

---

## Day 1 — March 28 (Saturday) — Backend Foundation

### Morning Block (0–4 hours)
**Goal: Project is running, DB is set up, Auth works**

| Time | Task IDs | Deliverable |
|------|----------|-------------|
| Hour 1 | T001–T003 | Spring Boot + React projects initialized, PostgreSQL connected |
| Hour 2 | T004–T007 | Flyway migrations run, all 11 tables created, seed data inserted |
| Hour 3 | T008–T012 | JWT security scaffolding — JwtUtil, JwtFilter, SecurityConfig |
| Hour 4 | T013–T015 | Auth endpoints working — login returns JWT ✅ |

### Afternoon Block (4–8 hours)
**Goal: All JPA entities + exception framework done**

| Time | Task IDs | Deliverable |
|------|----------|-------------|
| Hour 5 | T016–T019 | Category, Supplier, Product, Order entities + repos |
| Hour 6 | T020–T023 | Sales, Alert, Bundle, AILog entities + repos |
| Hour 7 | T024–T026 | Exception classes + GlobalExceptionHandler |
| Hour 8 | T027–T028 | ProductService (CRUD) + ProductSearchService (overloaded) |

### Evening Block (8–14 hours)
**Goal: Product, Supplier, Orders, Sales — all API endpoints working**

| Time | Task IDs | Deliverable |
|------|----------|-------------|
| Hour 9 | T029–T033 | StockService, ProductController, SKUValidator, ProductCounter, StringUtils |
| Hour 10 | T034–T038 | Searchable interface, Person/Employee hierarchy, SupplierService + Controller |
| Hour 11 | T039–T042 | OrderQueue (PriorityQueue + Cloneable), OrderService, OrderController, NotificationQueue |
| Hour 12 | T043–T045 | SalesService (with file logging), SalesController, SalesAverageCalculator |
| Hour 13 | T046–T049 | AlertService, AlertController, AlertScheduler, EmailService (Gmail SMTP) |
| Hour 14 | **Checkpoint** | Core WMS API functional — test all endpoints with Postman ✅ |

---

## Day 2 — March 29 (Sunday) — AI Engine + Analytics + Frontend

### Morning Block (14–20 hours)
**Goal: Recommendation engine, analytics, and file I/O complete**

| Time | Task IDs | Deliverable |
|------|----------|-------------|
| Hour 15 | T050–T052 | AIConfig (WebClient), RecommendationService (Claude API calls), RecommendationController |
| Hour 16 | T053–T054 | Wire AI into product creation, bundle save endpoints |
| Hour 17 | T055–T057 | Analytics queries (MoM/QoQ/YoY/Custom), AnalyticsService, AnalyticsController |
| Hour 18 | T058–T061 | FileExportService (CSV/TXT), FileImportService, InventoryLogWriter, RecursiveUtils |
| Hour 19 | T062–T065 | TagDeduplicator, FileController, Logback config, BackupScheduler |
| Hour 20 | T066–T071 | All remaining OOPJ utils (MathUtils, TextAnalyzer, VowelTracker, BarcodeUtils, WarehouseZone, C demos) |

### Afternoon Block (20–30 hours)
**Goal: React frontend — all pages functional**

| Time | Task IDs | Deliverable |
|------|----------|-------------|
| Hour 21 | T072–T076 | React setup, Axios, AuthContext, PrivateRoute, Layout components |
| Hour 22 | T077–T078 | LoginPage + DashboardPage (stats cards + sales chart) |
| Hour 23 | T079–T080 | ProductsPage (table + search) + ProductDetailPage (bundle modal) |
| Hour 24 | T081–T082 | SuppliersPage + OrdersPage |
| Hour 25 | T083–T084 | SalesPage + AnalyticsPage (period comparison chart) |
| Hour 26 | T085–T086 | AlertsPage (AI tips accordion) + SettingsPage |
| Hour 27 | T087–T091 | Shared components: Modal, Table, BundleModal, StockUpdateModal, SalesChart |
| Hour 28 | **Checkpoint** | Full stack running — all pages load, forms submit ✅ |

---

## Day 3 — March 30 (Monday) — Integration, Polish & Submission

### Morning Block (30–38 hours)
**Goal: All flows tested, docs written, project submitted**

| Time | Task IDs | Deliverable |
|------|----------|-------------|
| Hour 29–30 | T092–T094 | E2E test: Login → Product → Alert → Email flow |
| Hour 31 | T095–T097 | E2E test: Analytics, File export/import, Role guards |
| Hour 32 | T098–T099 | DemoRunner for OOPJ verification, tick off coverage matrix |
| Hour 33–34 | T100–T103 | README, env example, JavaDoc, OOPJ_COVERAGE.md |
| Hour 35 | — | Final review: fix any broken flows, clean console errors |
| Hour 36 | — | **SUBMIT** ✅ |

---

## MVP Priority (If Pressed for Time)

If hours run short, maintain this priority order:

### Must Have (Core Grade)
1. Auth (login/JWT)
2. Product CRUD + Stock-In/Out
3. Alert creation on threshold breach
4. Email notification
5. All OOPJ utility classes (T066–T071, T031–T033)
6. Analytics (at least MoM comparison)

### Should Have (Strong Grade)
7. AI bundle recommendations
8. Supplier + Order management
9. File export/import
10. Full React frontend

### Nice to Have (Excellence)
11. AI overstock strategies
12. Scheduled daily checker
13. Dashboard stats + charts
14. Settings page

---

## Environment Setup Checklist

```bash
# Prerequisites
java --version        # Must be 17+
mvn --version         # Must be 3.9+
node --version        # Must be 18+
psql --version        # Must be 15+

# Backend startup
cd backend
cp application.properties.example application.properties
# Edit: DB credentials, JWT secret, Gmail app password, Claude API key
mvn spring-boot:run

# Frontend startup
cd frontend
npm install
npm run dev
# Open http://localhost:5173

# Default login
# Email:    admin@wms.com
# Password: admin123
```

---

## Environment Variables Required

```properties
# application.properties

# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/wms_ai_db
spring.datasource.username=YOUR_PG_USERNAME
spring.datasource.password=YOUR_PG_PASSWORD

# JWT
app.jwt.secret=YOUR_256_BIT_SECRET_KEY
app.jwt.expiration=28800000

# Gmail SMTP
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=YOUR_GMAIL@gmail.com
spring.mail.password=YOUR_GMAIL_APP_PASSWORD
spring.mail.properties.mail.smtp.starttls.enable=true

# Claude AI API
anthropic.api.key=YOUR_CLAUDE_API_KEY
anthropic.api.model=claude-sonnet-4-20250514

# Alert config
app.alert.admin.email=admin@wms.com
app.alert.default.overstock.days=30
app.alert.default.min.threshold=50

# File paths
app.files.base.path=/wms-data
```

---

## Risk Register

| Risk | Probability | Mitigation |
|------|-------------|------------|
| Claude API key unavailable | Medium | Rule-based fallback already designed (T051) |
| Gmail SMTP blocked | Medium | Use App Password (not account password); test early in Hour 13 |
| Postgres connection failure | Low | Fallback: SQLite mode with H2 for demo |
| React build errors | Low | Build after every page; don't leave frontend to last hour |
| 48-hour deadline too tight | High | Follow MVP priority list above strictly |
| OOPJ coverage missed | Low | DemoRunner (T098) validates all 30 items on startup |
