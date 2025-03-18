# FitzCode - 의류 쇼핑몰 플랫폼

**FitzCode**은 다양한 브랜드의 의류를 한곳에서 비교하고, 간편하게 구매할 수 있는 **온라인 쇼핑몰 플랫폼**입니다.  
사용자 친화적인 UI와 안정적인 백엔드 시스템을 통해 원활한 쇼핑 경험을 제공합니다.

---

## 기술 스택

### 웹 인터페이스 (Frontend)
- **Thymeleaf**
- **HTML5, CSS3, JavaScript**
- **Ajax, Fetch API, jQuery**
- **Figma** (디자인)

### 서버 개발 (Backend)
- **Java 17**
- **Spring Boot 3.4.3**
- **Spring Security**
- **Spring Batch**
- **SSE (Server-Sent Events)**
- **Apache POI**

### 데이터 관리 (Database)
- **MyBatis**
- **MySQL 8.0.39**
- **Amazon RDS**

### 배포 및 인프라 (DevOps)
- **Docker**
- **Jenkins**
- **Amazon EC2**
- **Amazon S3**
- **PortOne**

### 개발 도구 (Tools)
- **IntelliJ IDEA, DataGrip**
- **Git, GitKraken**
- **Postman**
- **Jasypt**
- **Notion**
- **Swagger**

---

## 주요 기능

1. **카테고리 기반 상품 탐색**
   - 의류, 신발 등 다양한 카테고리 제공
   - 카테고리별 필터링 기능 지원

2. ** 사용자 스타일 업로드 **
   - 사용자는 자신의 스타일을 사진과 함께 업로드할 수 있음.
	 - 스타일별 해시태그(#캐주얼, #스트릿, #포멀 등) 추가 가능.
	 - 좋아요 및 댓글 기능 지원하여 사용자 간 소통 가능.
	 - 인기 스타일 랭킹 제공.

2. **비동기 데이터 처리**
   - **AJAX & Fetch API**를 활용한 실시간 데이터 반영
   - 장바구니, 주문 상태 업데이트 등 페이지 새로고침 없이 처리

3. **보안 강화 및 결제 시스템 연동**
   - **Spring Security** 기반의 사용자 인증 및 권한 관리
   - **PortOne**을 통한 간편 결제 지원

4. **관리자 기능**
   - **Spring Batch**를 활용한 정기적인 데이터 업데이트
   - 판매 통계 및 사용자 관리 기능 제공

5. **실시간 알림 시스템**
   - **SSE (Server-Sent Events)** 기반으로 주문 및 배송 알림 제공
