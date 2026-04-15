# WMS-AI: Artificial Intelligence Warehouse Management System

An advanced, full-stack, enterprise-grade generic Warehouse Management System powered by the Anthropic Claude Engine to deliver realtime insights, automated ordering schedules, optimal bundle configurations, and interactive sales tracking.

## Technologies Used
- **Backend:** Java 25, Spring Boot 3.5
- **Database:** H2 In-Memory Database (Persisted) / Hibernate JPA
- **Security:** Spring Security, stateless JSON Web Tokens (JWT)
- **Frontend Integration:** Vanilla HTML/JS, Tailwind CSS, API REST fetching
- **Integration Frameworks:** Spring Mail (SMTP), `spring-boot-starter-validation`

---

## 🛠 Project Setup

### 1. Database & Config Preparation

1. Open `backend/application.properties.example`.
2. Save it as `backend/src/main/resources/application.properties`.
3. Provide your secure strings:
   - **`app.jwt.secret`**: Update to a large secure base64 AES encoded string (Minimum 256 bits).
   - **`spring.mail.password`**: Your generated 16-Digit App Password from Google if using Gmail.
   - **`anthropic.api.key`**: Your specialized Auth key from Anthropic to generate dynamic AI Insights.

### 2. Booting the Backend Server

To compile the codebase and spin up the server, run:
```bash
cd backend
./mvnw clean spring-boot:run
```
Upon start, the local H2 DB will be hydrated efficiently with our schema representations + Sample data (e.g. Products, Suppliers, Order tables).
Also, notice console logging for our OOPJ-specific metrics indicating successful `DemoRunner` completion of all 30 Practical assignments natively inside Java execution.

The Server operates permanently on `http://localhost:8080`.

### 3. Loading the Client Interface

Our frontend sits in the `stitch_wms_ai_frontend_dashboard` folder.
To view the UI, simply serve those HTML files.
For example, using Python:
```bash
cd stitch_wms_ai_frontend_dashboard
python -m http.server 3000
```
Then navigate to `http://localhost:3000/login_page/code.html` to authenticate using the static seed credentials:
- **Email:** admin@wms.com
- **Password:** admin123 

---

## 📂 Academic Coverage

This project strictly follows the OOPJ course requirements! To review exactly how each metric was solved sequentially, see our detailed [OOPJ Coverage Matrix](OOPJ_COVERAGE.md).
