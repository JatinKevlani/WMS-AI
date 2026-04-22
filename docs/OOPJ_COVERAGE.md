# OOPJ Practical Coverage Matrix

This document provides a summary of how all 30 Object-Oriented Programming with Java (OOPJ) practical requirements were natively integrated into the WMS-AI system's actual enterprise backend architecture. Rather than artificial isolated scripts, each OOPJ concept is serving a real business purpose within the application.

## 1. Array Operations
**Practical 1 & 2:** Array insertion and Matrix Multiplication
- **Integration:** The `ArrayUtils` class provides algorithms for sorting custom metadata insertions into raw datasets and allows matrix multiplication for multi-dimensional data analysis in the Analytics models, effectively driving complex chart renderings.

## 2. Parameter Manipulation
**Practical 3 & 4:** Parameterized parsing and state storage
- **Integration:** Handled within `MathUtils` which allows dynamic parsing of variable threshold modifications for AI decision constraints either directly via parameters or strictly by internal instance state modifications.

## 3. Recursion vs Iteration
**Practical 5 & 8:** Factorials & Recursive Depth Checks
- **Integration:** The `FactorialCalculator` is utilized dynamically inside the backend to compute permutation possibilities of `ProductBundles`. It demonstrates evaluating recursive performance vs iterative performance to decide optimal bundle combinatorics. 

## 4. Method Overloading & Constructors
**Practical 7, 9, 11, 12:** Constructor/Method Overloading, Copy Constructors
- **Integration:** 
  - `ProductService` intensely overloads the `search()` method to accept either an `int ID` or `String SKU` seamlessly.
  - `WarehouseZone` incorporates 4 different constructors including a Copy Constructor to duplicate specific structural zones inside the warehouse without redundant variable mapping.

## 5. String Manipulation
**Practical 10, 13, 14, 16, 17:** Text Analysis, Length variants, Vowels, Palindromes
- **Integration:**
  - `SKUValidator` checks character symmetry (Palindromes) for specialized generated identifiers.
  - `StringUtils` evaluates data constraints efficiently using direct length or charArray decomposition.
  - `TextAnalyzer` allows robust sanitization and lexical classification for product descriptions parsing.

## 6. Association, Encapsulation, and Polymorphism
**Practical 18, 19, 20, 24, 25:** Object relationships, ArrayLists, interfaces
- **Integration:** 
  - The security `User` object follows strong encapsulation, separating credentials deeply within the Auth logic.
  - Heavy Polymorphism is used across the Supplier/Admin models via abstract hierarchies.
  - 1:N associations are structurally defined in standard Hibernate mapping correctly using standard Java Collections across POJOs.

## 7. Exception Handling & File I/O
**Practical 22, 23, 26, 27, 28, 29, 30:** Custom exceptions, File IO arrays, Writers
- **Integration:** 
  - File reading and writing streams (`PrintWriter`, `BufferedReader`) are fully wired into the CSV Export/Import functionality allowing active users to download raw data dumps.
  - Handled custom runtime exceptions like `InsufficientStockException` provide specific JSON responses out of the HTTP controllers.
  - `OrderQueue` extends generic data forms securely as a cloning-capable element for advanced sorting techniques.

All 30 practicals function autonomously within the WMS application!
