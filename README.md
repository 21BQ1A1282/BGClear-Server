<h2 align="center">
   🎨 BGClear Backend
</h2>

<h3>Spring Boot REST API for AI background removal with payment integration.</h3>

## 🚀 Quick Start
   1. **Clone the repository**
      ```bash
      git clone https://github.com/21BQ1A1282/BGClear-Server.git
      cd BGClear-Server
      ```

   2. **Clean or run application**
      ```bash
      mvn clean install
      mvn spring-boot:run
      ```


## 🔧 Configuration

  1. Configure or Edit application.properties
     ```properties
     # Required environment variables and configurations
     clerk.issuer=your-clerk-issuer
     razorpay.key.id=your-razorpay-key
     razorpay.key.secret=your-razorpay-secret
     clipdrop.api-key=your-clipdrop-key
     spring.datasource.url=jdbc:postgresql://localhost:5432/bgclear_db
     ....
     ```

## 🏗️ Architecture
- **Framework**: Spring Boot 3.x + Java 17
- **Database**: PostgreSQL with JPA
- **Security**: JWT authentication with Clerk
- **Payments**: Razorpay integration
- **AI Processing**: Clipdrop API

## 💡 Core Features
### Authentication & Security
- JWT validation with Clerk JWKS
- Webhook signature verification
- CORS configured for frontend

### Image Processing
- Clipdrop AI integration
- Multipart file upload (10MB max)
- Base64 response encoding
- Credit-based access control

### Payment System
- Razorpay order creation
- Payment verification
- Credit management
- Prevents double spending

### User Management
- Sync with Clerk authentication
- Credit balance tracking
- Webhook handlers for user events


## 🔄 Key Flows
### Image Processing
1. Validate JWT token and credits
2. Send image to Clipdrop API
3. Deduct 1 credit on success
4. Return processed image

### Payment Flow
1. Create Razorpay order
2. User completes payment
3. Verify payment with Razorpay
4. Add credits to user account

Frontend required for full functionality: [BGClear Frontend](https://github.com/21BQ1A1282/BGClear-Client)
