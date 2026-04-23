```mermaid
classDiagram
    class AIRecommendationLog {
    -Integer id
    -String recommendationType
    -Product product
    -String promptSent
    -String responseReceived
    -LocalDateTime createdAt
    #void onCreate()
}
    class AIRecommendationLogRepository {
    <<interface>>
}
    class AdminUser {
    -String accessLevel
    +String getAccessLevel()
    +String getDisplayInfo()
}
    class Alert {
    -Integer id
    -AlertType type
    -Product product
    -String message
    -Boolean isRead
    -LocalDateTime createdAt
    #void onCreate()
}
    class AlertController {
    -AlertService alertService
    +ResponseEntity_List_ getUnreadAlerts()
    +ResponseEntity_Map_ markAsRead(int id)
    +ResponseEntity_Map_ getUnreadCount()
}
    class AlertRepository {
    <<interface>>
    _List_Alert_ findByIsReadFalseOrderByCreatedAtDesc()
    _long countByIsReadFalse()
    _boolean existsByProductIdAndTypeAndCreatedAtAfter(Integer productId_ AlertType type_ LocalDateTime after)
}
    class AlertScheduler {
    -ProductRepository productRepository
    -AlertService alertService
    +void dailyThresholdCheck()
}
    class AlertService {
    -AlertRepository alertRepository
    -EmailService emailService
    +void checkThresholds(Product product)
    +void createAlert(AlertType type_ Product product_ String message)
    +List_Alert_ getUnreadAlerts()
    +void markAsRead(int alertId)
    +long getUnreadCount()
}
    class AlertType {
    <<enumeration>>
}
    class AreaCalculator {
    +double calculateArea(double length_ double width)
    +double calculateArea(double radius)
    +double calculateTriangleArea(double base_ double height)
    +String displayCalculations(double l_ double w_ double r_ double base_ double h)
}
    class ArrayUtils {
    +static int insertSorted(int arr_ int element)
    +static int multiplyMatrices(int a_ int b)
    +static String matrixToString(int matrix)
}
    class AuditLogService {
    -String basePath
    +void logActivity(String action_ String user_ String details)
    +String readActivityLog()
    +String readRecentLog(int lines)
}
    class AuthController {
    -AuthService authService
    +ResponseEntity_AuthResponse_ login(LoginRequest request)
    +ResponseEntity_AuthResponse_ register(RegisterRequest request)
    +ResponseEntity_List_ getVisibleUsers(UserDetails userDetails)
    +ResponseEntity_Map_ logout()
}
    class AuthResponse {
    -String id
    -String token
    -String email
    -String fullName
    -String role
    -boolean active
}
    class AuthService {
    -UserRepository userRepository
    -PasswordEncoder passwordEncoder
    -JwtUtil jwtUtil
    -AuthenticationManager authenticationManager
    +AuthResponse login(LoginRequest request)
    +AuthResponse register(RegisterRequest request)
    +java getVisibleUsers(String requesterEmail)
}
    class BackupScheduler {
    -ProductRepository productRepository
    -String basePath
    +void dailyInventoryBackup()
    -String truncate(String s_ int maxLen)
}
    class BarcodeUtils {
    +static int bin2Dec(String binaryString)
    +static String dec2Bin(int decimal)
}
    class Category {
    -Integer id
    -String name
    -String description
    -LocalDateTime createdAt
    #void onCreate()
}
    class CategoryController {
    -CategoryRepository categoryRepository
    -ProductRepository productRepository
    +ResponseEntity_List_ getAll()
    +ResponseEntity_Category_ create(Category category)
    +ResponseEntity_Category_ update(int id_ Category dto)
    +ResponseEntity_Map_ delete(int id)
}
    class CategoryRepository {
    <<interface>>
    _Optional_Category_ findByName(String name)
    _boolean existsByName(String name)
}
    class CustomUserDetailsService {
    -UserRepository userRepository
    +UserDetails loadUserByUsername(String email)
}
    class DataInitializer {
    -UserRepository userRepository
    -CategoryRepository categoryRepository
    -PasswordEncoder passwordEncoder
    +void run(String args)
    -void seedAdminUser()
}
    class DuplicateSKUException {
}
    class EmailService {
    -JavaMailSender mailSender
    -String adminEmail
    +void sendAlertEmail(Alert alert)
}
    class Employee {
    -int employeeId
    -String department
    +int getEmployeeId()
    +void setEmployeeId(int employeeId)
    +String getDepartment()
    +void setDepartment(String department)
    +String getDisplayInfo()
    +Object searchById(int id)
    +Object searchByName(String name)
}
    class FactorialCalculator {
    +static long factorialRecursive(int n)
    +static long factorialIterative(int n)
    +static String factorialDisplay(int n)
}
    class FileController {
    -FileExportService fileExportService
    -FileImportService fileImportService
    -AuditLogService auditLogService
    +ResponseEntity_Resource_ exportProducts()
    +ResponseEntity_Resource_ exportSales()
    +ResponseEntity_Resource_ exportReport()
    +ResponseEntity_Map_ importProducts(MultipartFile file)
    +ResponseEntity_Map_ getActivityLog(int lines)
    -ResponseEntity_Resource_ downloadFile(String filePath_ String downloadName)
}
    class FileExportService {
    -ProductRepository productRepository
    -SalesRepository salesRepository
    -String basePath
    +String exportProductsCsv()
    +String exportSalesCsv()
    +String generateInventoryReport()
    -String timestamp()
    -String escapeCsv(String value)
    -String truncate(String s_ int maxLen)
}
    class FileImportService {
    -ProductRepository productRepository
    -CategoryRepository categoryRepository
    +int importProductsCsv(MultipartFile file)
    -String parseCsvLine(String line)
}
    class GlobalExceptionHandler {
    +ResponseEntity_Map_ handleAuthenticationFailure(Exception ex)
    +ResponseEntity_Map_ handleAccessDenied(AccessDeniedException ex)
    +ResponseEntity_Map_ handleInsufficientStock(InsufficientStockException ex)
    +ResponseEntity_Map_ handleDuplicateSKU(DuplicateSKUException ex)
    +ResponseEntity_Map_ handleProductNotFound(ProductNotFoundException ex)
    +ResponseEntity_Map_ handleInvalidSKU(InvalidSKUException ex)
    +ResponseEntity_Map_ handleIllegalArg(IllegalArgumentException ex)
    +ResponseEntity_Map_ handleValidation(MethodArgumentNotValidException ex)
    +ResponseEntity_Map_ handleUnreadableBody(HttpMessageNotReadableException ex)
    +ResponseEntity_Map_ handleGeneric(Exception ex)
    -String formatFieldError(FieldError error)
    -ResponseEntity_Map_ buildError(HttpStatus status_ String message)
}
    class InsufficientStockException {
}
    class InvalidSKUException {
}
    class InventoryItem {
    -int itemId
    -String name
    -int quantity
    -double price
    +int getItemId()
    +String getName()
    +int getQuantity()
    +double getPrice()
    +String toString()
}
    class InventoryLogWriter {
    -static int COUNT
    +static int writeRandomStockLog(String filePath)
    +static int readAndFindSmallest(String filePath)
    +static int writeAndFindSmallest(String filePath)
}
    class JacksonConfig {
    +ObjectMapper objectMapper()
}
    class JwtFilter {
    -JwtUtil jwtUtil
    -CustomUserDetailsService userDetailsService
    #void doFilterInternal(HttpServletRequest request_ HttpServletResponse response_ FilterChain filterChain)
}
    class JwtUtil {
    -SecretKey key
    -long expiration
    +String generateToken(String email_ String role)
    +String extractUsername(String token)
    +String extractRole(String token)
    +boolean validateToken(String token_ UserDetails userDetails)
    -boolean isTokenExpired(String token)
    -Claims parseClaims(String token)
}
    class LoginRequest {
    -String email
    -String password
}
    class MathUtils {
    -int a
    -int b
    +static int sumWithParams(int a_ int b)
    +int sumWithoutParams()
    +void setA(int a)
    +void setB(int b)
    +int getA()
    +int getB()
}
    class NotificationQueue {
    +void addNotification(Object item)
    +void printAll()
    +String getSummary()
}
    class OrderController {
    -OrderService orderService
    -UserRepository userRepository
    +ResponseEntity_List_ getAllOrders()
    +ResponseEntity_PurchaseOrder_ getOrderById(int id)
    +ResponseEntity_PurchaseOrder_ createOrder(OrderDTO dto_ UserDetails userDetails)
    +ResponseEntity_PurchaseOrder_ updateStatus(int id_ Map_String_String_ body)
}
    class OrderDTO {
    -Integer supplierId
    -String notes
    -List_OrderItemDTO_ items
}
    class OrderItem {
    -Integer id
    -PurchaseOrder order
    -Product product
    -Integer quantity
    -BigDecimal unitPrice
}
    class OrderItemDTO {
    -Integer productId
    -Integer quantity
    -BigDecimal unitPrice
}
    class OrderQueue {
    +OrderQueue clone()
    +String getQueueSummary()
}
    class OrderRepository {
    <<interface>>
    _List_PurchaseOrder_ findBySupplierId(Integer supplierId)
    _List_PurchaseOrder_ findByStatus(OrderStatus status)
}
    class OrderService {
    -OrderRepository orderRepository
    -SupplierRepository supplierRepository
    -ProductRepository productRepository
    -AlertService alertService
    +PurchaseOrder createOrder(OrderDTO dto_ User createdBy)
    +PurchaseOrder updateStatus(int orderId_ String newStatus)
    +List_PurchaseOrder_ getAllOrders()
    +PurchaseOrder getOrderById(int id)
    +List_PurchaseOrder_ getOrdersByStatus(String status)
}
    class OrderStatus {
    <<enumeration>>
}
    class Person {
    <<abstract>>
    -String name
    -String email
    -String phone
    +String getName()
    +void setName(String name)
    +String getEmail()
    +void setEmail(String email)
    +String getPhone()
    +void setPhone(String phone)
    +abstract String getDisplayInfo()
    +String toString()
}
    class Product {
    -Integer id
    -String sku
    -String name
    -Category category
    -Supplier supplier
    -BigDecimal unitPrice
    -Integer quantity
    -Integer minThreshold
    -Integer maxThreshold
    -Integer overstockDays
    -Boolean isDeleted
    -LocalDate lastSaleDate
    -LocalDateTime createdAt
    -LocalDateTime updatedAt
    #void onCreate()
    #void onUpdate()
}
    class ProductBundle {
    -Integer id
    -Product product
    -String bundleProductName
    -String reason
    -LocalDateTime savedAt
}
    class ProductBundleRepository {
    <<interface>>
    _List_ProductBundle_ findByProductId(Integer productId)
}
    class ProductController {
    -ProductService productService
    -StockService stockService
    +ResponseEntity_Page_ getAllProducts(Pageable pageable)
    +ResponseEntity_Product_ getProductById(int id)
    +ResponseEntity_List_ searchProducts(String q)
    +ResponseEntity_Product_ createProduct(ProductDTO dto)
    +ResponseEntity_Product_ updateProduct(int id_ ProductDTO dto)
    +ResponseEntity_Map_ deleteProduct(int id)
    +ResponseEntity_Product_ stockIn(int id_ Map_String_Integer_ body)
    +ResponseEntity_Product_ stockOut(int id_ Map_String_Integer_ body)
}
    class ProductCounter {
    -static int totalCount
    +static void increment()
    +static int getCount()
    +static void reset()
}
    class ProductDTO {
    -String name
    -String sku
    -Integer categoryId
    -Integer supplierId
    -BigDecimal unitPrice
    -Integer quantity
    -Integer minThreshold
    -Integer maxThreshold
    -Integer overstockDays
}
    class ProductNotFoundException {
}
    class ProductRepository {
    <<interface>>
    _Optional_Product_ findBySku(String sku)
    _boolean existsBySku(String sku)
    _Page_Product_ findByIsDeletedFalse(Pageable pageable)
    _long countByIsDeletedFalse()
    _List_Product_ searchByNameOrSku(String query)
    _List_Product_ searchByNameAndCategory(String name_ String category)
    _List_Product_ findBySupplierId(Integer supplierId)
    _List_Product_ findLowStockProducts()
    _List_Product_ findOverstockProducts(LocalDate cutoffDate)
}
    class ProductService {
    -ProductRepository productRepository
    -CategoryRepository categoryRepository
    -SupplierRepository supplierRepository
    -AIRecommendationService aiRecommendationService
    +Page_Product_ getAllProducts(Pageable pageable)
    +Product getProductById(int id)
    +Product createProduct(ProductDTO dto)
    +Product updateProduct(int id_ ProductDTO dto)
    -Category resolveCategory(Integer categoryId)
    -Supplier resolveSupplier(Integer supplierId)
    +void softDeleteProduct(int id)
    +List_Product_ search(String query)
    +List_Product_ search(int id)
    +List_Product_ search(String name_ String category)
}
    class PurchaseOrder {
    -Integer id
    -Supplier supplier
    -OrderStatus status
    -BigDecimal totalAmount
    -String notes
    -User createdBy
    -List_OrderItem_ items
    -LocalDateTime createdAt
    -LocalDateTime updatedAt
    #void onCreate()
    #void onUpdate()
    +int compareTo(PurchaseOrder other)
    +void addItem(OrderItem item)
}
    class RecommendationController {
    -AIRecommendationService aiService
    +ResponseEntity_Map_ getRestockRecommendation(int productId)
    +ResponseEntity_Map_ getBundleRecommendation(int productId)
    +ResponseEntity_Map_ getDemandForecast(int productId)
    +ResponseEntity_ProductBundle_ saveBundle(Map_String_String_ body)
}
    class RecursiveUtils {
    +static int findSmallest(int arr_ int index)
    +static int findSmallest(int arr)
    +static int findLargest(int arr_ int index)
}
    class RegisterRequest {
    -String email
    -String password
    -String fullName
    -String role
}
    class Role {
    <<enumeration>>
}
    class SKUValidator {
    +static boolean isValidSKU(String sku)
    +static boolean isPalindrome(String sku)
}
    class SalesAverageCalculator {
    +static double averageInt(int values)
    +static double averageDouble(double values)
    +static BigDecimal averageBigDecimal(List_BigDecimal_ values)
}
    class SalesController {
    -SalesService salesService
    -UserRepository userRepository
    +ResponseEntity_List_ getTransactions(String from_ String to)
    +ResponseEntity_SalesTransaction_ recordSale(SalesDTO dto_ UserDetails userDetails)
}
    class SalesDTO {
    -Integer productId
    -Integer quantitySold
    -BigDecimal salePrice
}
    class SalesRepository {
    <<interface>>
    _List_SalesTransaction_ findBySaleDateBetween(LocalDateTime from_ LocalDateTime to)
    _List_SalesTransaction_ findBySaleDateBetweenOrderBySaleDateAsc(LocalDateTime from_ LocalDateTime to)
    _List_SalesTransaction_ findByProductId(Integer productId)
    _BigDecimal findRevenueByPeriod(LocalDateTime from_ LocalDateTime to)
    _List_Object_ findTopSellingProducts(LocalDateTime from_ LocalDateTime to)
    _List_Object_ findRevenueByCategory(LocalDateTime from_ LocalDateTime to)
    _List_Object_ findSlowMovingProducts(LocalDateTime from_ LocalDateTime to)
}
    class SalesService {
    -SalesRepository salesRepository
    -ProductRepository productRepository
    -StockService stockService
    -AuditLogService auditLogService
    +SalesTransaction recordSale(SalesDTO dto_ User soldBy)
    +List_SalesTransaction_ getTransactions(LocalDateTime from_ LocalDateTime to)
    +List_SalesTransaction_ getTransactionsByProduct(int productId)
    +BigDecimal getTotalRevenue(LocalDateTime from_ LocalDateTime to)
    +List_SalesTransaction_ getAllTransactions()
}
    class SalesTransaction {
    -Integer id
    -Product product
    -Integer quantitySold
    -BigDecimal salePrice
    -BigDecimal totalAmount
    -User soldBy
    -LocalDateTime saleDate
    #void onCreate()
}
    class Searchable {
    <<interface>>
    _Object searchById(int id)
    _Object searchByName(String name)
}
    class SecurityConfig {
    -JwtFilter jwtFilter
    -String allowedOrigins
    +PasswordEncoder passwordEncoder()
    +AuthenticationManager authenticationManager(AuthenticationConfiguration config)
    +SecurityFilterChain securityFilterChain(HttpSecurity http)
    +CorsConfigurationSource corsConfigurationSource()
}
    class SettingsController {
    -EntityManager em
    -UserRepository userRepository
    +ResponseEntity_List_ getAll()
    +ResponseEntity_Map_ updateSettings(Map_String_String_ settings_ UserDetails userDetails)
}
    class StaffUser {
    -String shift
    +String getShift()
    +String getDisplayInfo()
}
    class StockService {
    -ProductRepository productRepository
    -AlertService alertService
    +Product stockIn(int productId_ int qty)
    +Product stockOut(int productId_ int qty)
}
    class StringUtils {
    +static int lengthUsingLength(String s)
    +static int lengthUsingToCharArray(String s)
}
    class Supplier {
    -Integer id
    -String name
    -String contactPerson
    -String email
    -String phone
    -String address
    -LocalDateTime createdAt
    #void onCreate()
}
    class SupplierController {
    -SupplierRepository supplierRepository
    -ProductRepository productRepository
    +ResponseEntity_List_ getAll()
    +ResponseEntity_Map_ getById(int id)
    +ResponseEntity_Supplier_ create(Supplier supplier)
    +ResponseEntity_Supplier_ update(int id_ Supplier dto)
    +ResponseEntity_List_ search(String q)
}
    class SupplierRepository {
    <<interface>>
    _List_Supplier_ findByNameContainingIgnoreCase(String name)
}
    class SupplierService {
    -SupplierRepository supplierRepository
    +List_Supplier_ getSupplierList()
    +Supplier createSupplier(Supplier supplier)
    +Object searchById(int id)
    +Object searchByName(String name)
}
    class SystemSetting {
    -String key
    -String value
    -User updatedBy
    -LocalDateTime updatedAt
}
    class SystemSettingRepository {
    <<interface>>
}
    class TagDeduplicator {
    +static List_String_ readAndDeduplicate(String filePath)
    +static List_String_ deduplicateAndSort(String words)
}
    class TextAnalyzer {
    +static Map_String_Integer_ countVowelsAndConsonants(String line)
    +static int countCapitalStartWords(String line)
}
    class User {
    -UUID id
    -String email
    -String passwordHash
    -String fullName
    -Role role
    -boolean isActive
    -LocalDateTime createdAt
    #void onCreate()
}
    class UserRepository {
    <<interface>>
    _Optional_User_ findByEmail(String email)
    _boolean existsByEmail(String email)
}
    class UserSummaryDTO {
    -String id
    -String email
    -String fullName
    -String role
    -boolean active
    -LocalDateTime createdAt
    +static UserSummaryDTO from(User user)
}
    class WarehouseZone {
    -String type
    -double volume
    -String zoneName
    +String getType()
    +double getVolume()
    +String getZoneName()
    +String toString()
}
    class WmsAiBackendApplication {
    +static void main(String args)
}
    ArrayList <|-- NotificationQueue
    Cloneable <|.. OrderQueue
    CommandLineRunner <|.. DataInitializer
    Comparable <|.. PurchaseOrder
    Employee <|-- AdminUser
    Employee <|-- StaffUser
    Exception <|-- InsufficientStockException
    JpaRepository <|-- AIRecommendationLogRepository
    JpaRepository <|-- AlertRepository
    JpaRepository <|-- CategoryRepository
    JpaRepository <|-- OrderRepository
    JpaRepository <|-- ProductBundleRepository
    JpaRepository <|-- ProductRepository
    JpaRepository <|-- SalesRepository
    JpaRepository <|-- SupplierRepository
    JpaRepository <|-- SystemSettingRepository
    JpaRepository <|-- UserRepository
    OncePerRequestFilter <|-- JwtFilter
    Person <|-- Employee
    PriorityQueue <|-- OrderQueue
    RuntimeException <|-- DuplicateSKUException
    RuntimeException <|-- InvalidSKUException
    RuntimeException <|-- ProductNotFoundException
    Searchable <|.. Employee
    Searchable <|.. SupplierService
    UserDetailsService <|.. CustomUserDetailsService
    AIRecommendationLog o-- Product
    Alert o-- AlertType
    Alert o-- Product
    AlertController o-- AlertService
    AlertScheduler o-- AlertService
    AlertScheduler o-- ProductRepository
    AlertService o-- AlertRepository
    AlertService o-- EmailService
    AuthController o-- AuthService
    AuthService o-- JwtUtil
    AuthService o-- UserRepository
    BackupScheduler o-- ProductRepository
    CategoryController o-- CategoryRepository
    CategoryController o-- ProductRepository
    CustomUserDetailsService o-- UserRepository
    DataInitializer o-- CategoryRepository
    DataInitializer o-- UserRepository
    FileController o-- AuditLogService
    FileController o-- FileExportService
    FileController o-- FileImportService
    FileExportService o-- ProductRepository
    FileExportService o-- SalesRepository
    FileImportService o-- CategoryRepository
    FileImportService o-- ProductRepository
    JwtFilter o-- CustomUserDetailsService
    JwtFilter o-- JwtUtil
    OrderController o-- OrderService
    OrderController o-- UserRepository
    OrderItem o-- Product
    OrderItem o-- PurchaseOrder
    OrderService o-- AlertService
    OrderService o-- OrderRepository
    OrderService o-- ProductRepository
    OrderService o-- SupplierRepository
    Product o-- Category
    Product o-- Supplier
    ProductBundle o-- Product
    ProductController o-- ProductService
    ProductController o-- StockService
    ProductService o-- CategoryRepository
    ProductService o-- ProductRepository
    ProductService o-- SupplierRepository
    PurchaseOrder o-- OrderStatus
    PurchaseOrder o-- Supplier
    PurchaseOrder o-- User
    SalesController o-- SalesService
    SalesController o-- UserRepository
    SalesService o-- AuditLogService
    SalesService o-- ProductRepository
    SalesService o-- SalesRepository
    SalesService o-- StockService
    SalesTransaction o-- Product
    SalesTransaction o-- User
    SecurityConfig o-- JwtFilter
    SettingsController o-- UserRepository
    StockService o-- AlertService
    StockService o-- ProductRepository
    SupplierController o-- ProductRepository
    SupplierController o-- SupplierRepository
    SupplierService o-- SupplierRepository
    SystemSetting o-- User
    User o-- Role
```
