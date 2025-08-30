# FitzCode — Clothing E-commerce Platform

FitzCode is an online shopping platform where users can compare apparel from various brands in one place and purchase conveniently.  
With a user-friendly UI and a reliable backend system, it provides a smooth shopping experience.


---

## Tech Stack

### Web Interface
- Thymeleaf
- HTML, CSS, JavaScript
- Ajax, Fetch API, jQuery
- Figma

### Backend
- Java 17
- Spring Boot 3.4.3
- SSE

### Data Management
- MySQL 8.0.39
- Amazon RDS

### Deployment & Infrastructure
- Docker
- Jenkins
- Amazon EC2
- Amazon S3

### External API / Connected Services
- PortOne
- Smart Delivery API
- Kakao login (OAuth2)
- Naver login (OAuth2)
- Kakao Map API

### Libraries
- Apache POI (Excel/Office document processing)  
- Jasypt (Encryption/Decryption)  
- Swagger (API documentation automation)  
- Spring Security (Authentication/Authorization)  
- Spring Batch (Large-scale batch processing)  
- MyBatis (SQL mapping framework)  


### DevTools
- IntelliJ
- DataGrip
- Git, GitKraken
- Postman
- Notion

---

## Key Features

### 1. User Management

- **User Registration & Authentication**  
  - Sign up and log in via email, phone number, or social login (Kakao, Naver)  
  - Differentiate users and admins by password and role (`role_id`)  
  - Email verification required to complete registration  
  - Password recovery via email verification  

- **User Information Management**  
  - Manage profile details such as profile image, nickname, date of birth  
  - Register and manage bank account and shipping address information  
  - Membership tier system: Automatically assigns BRONZE, SILVER, GOLD, or PLATINUM status based on total purchase amount (via trigger)  

---

### 2. Product Management

- **Product & Category Management**  
  - Register product details (name, price, stock, etc.) and classify into hierarchical categories (e.g., Shoes → Sneakers)  
  - Manage detailed images and stock by size  
  - Bulk product upload via Excel using Apache POI  

- **Admin Recommendations**  
  - Admins can recommend 10 products to users  

- **Product Q&A**  
  - Users can ask questions about products and admins can provide answers  

- **Product Reviews**  
  - Verified purchasers can leave reviews with ratings (1–5), text, and images  

---

### 3. Cart & Orders

- **Shopping Cart**  
  - Add products to cart and manage quantities  

- **Orders & Payments**  
  - Choose shipping address and pay via credit card, bank transfer, or simple payment methods  
  - Apply coupons, and receive order confirmation emails after payment  

- **Shipping Management**  
  - Track delivery status after order placement and receive notifications (integrated with Smart Delivery API)  

---

### 4. Refunds & Customer Support

- **Refund Processing**  
  - Request refunds for entire orders or individual items  
  - Manage refund status (Requested, In Progress, Completed)  

- **Customer Inquiries**  
  - Submit and manage general inquiries, product inquiries, and refund inquiries with admin responses  

---

### 5. Community Features

- **Posts & Management**  
  - Create posts by style category  
  - Upload thumbnails and additional images  
  - Tag products in posts  

- **Likes & Comments**  
  - Like posts and comments  
  - Write nested comments  

- **Bookmarks & Follows**  
  - Bookmark favorite posts  
  - Follow other users  

---

### 6. Notifications & Analytics

- **Push Notifications**  
  - Real-time notifications (via SSE) for announcements, inquiry responses, and shipping updates  

- **Search & Visit Analytics**  
  - Log user searches and visits  
  - Compare trending searches (yesterday vs today) using Spring Batch + Scheduler  

- **Error Logs**  
  - Record system errors for maintenance purposes  

---

### 7. Admin Features

- **Announcements**  
  - Create announcements with images/attachments  

- **Role & Access Management**  
  - Manage user roles (user, admin, logistics manager, etc.) and configure menu access permissions  

- **Transaction Logs**  
  - Record purchase, refund, and order cancellation history  

