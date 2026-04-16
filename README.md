# CareerVex Job Portal - Backend

## 📌 Project Description

A Job Portal Backend Application is built using Java and Spring Boot that powers a 
full-featured job portal platform. It provides secure and scalable REST APIs for
managing job listings, applications, and user interactions across different 
roles such as recruiters and candidates.

This backend handles secure communication, role-based access control, and 
scalable data management for a modern job portal system.

## ✨ Features

- JWT-based Authentication & Authorization
- RESTful API design
- User Registration & Login (Email/Phone)
- Role-based Access (Seeker, Recruiter, Admin)
- Profile Management (Education, Experience, Skills, Resume)
- Job Posting & Management (CRUD)
- Job Search Functionality
- Apply for Jobs & Application Status Updates
- Secure API Endpoints with Spring Security

## 🛠️ Tech Stack

- **Language:** Java
- **Backend:** Spring Boot
- **Security:** Spring Security + JWT
- **Database:** PostgreSQL (configurable)
- **ORM:** Hibernate (JPA)
- **Build Tool:** Maven / Gradle
- **Validation:** Jakarta Validation
- **Logging:** SLF4J
- **Lombok:** For boilerplate reduction

## 📁 Project Structure

All source code is organized under:
```
com.gokul.careervexjobportal
│
├── config          # Security configuration
├── controller      # REST controllers
├── dto             # Data Transfer Objects
├── model           # Entity classes
│   └── enums       # Enums (Role, Skills, Status)
├── repository      # JPA repositories
├── security        # JWT utilities & filters
├── service         # Business logic
```

## 🏗️ Architecture

This project follows a layered architecture to ensure clean separation of concerns, 
scalability, and maintainability.
```
Client → Controller → DTO → Service → Entity → Database
                         ↓
        Client ← Controller ← DTO ← Service
```

## 🔗 API Endpoints

### Auth APIs
```
| Method | Endpoint                    | Description     |
| ------ | --------------------------- | --------------- |
| POST   | `/api/auth/register`        | Register user   |
| POST   | `/api/auth/login`           | Login user      |
| PUT    | `/api/auth/forget_password` | Update password |
```
### Job APIs
```
| Method | Endpoint                    | Description            |
| ------ | --------------------------- | ---------------------- |
| GET    | `/api/jobs`                 | Get all jobs           |
| GET    | `/api/jobs/{id}`            | Get job by ID          |
| POST   | `/api/jobs`                 | Post a job (Recruiter) |
| PUT    | `/api/jobs/{id}`            | Update job             |
| DELETE | `/api/jobs/{id}`            | Delete job             |
| GET    | `/api/jobs/search?keyword=` | Search jobs            |
| GET    | `/api/jobs/my`              | Recruiter's jobs       |
```
### Application APIs
```
| Method | Endpoint                          | Description                |
| ------ | --------------------------------- | -------------------------- |
| POST   | `/api/applications/apply/{jobId}` | Apply to job               |
| GET    | `/api/applications/my`            | Get my applications        |
| GET    | `/api/applications/job/{jobId}`   | Get applicants (Recruiter) |
| PUT    | `/api/applications/{id}/status`   | Update status              |

```
### Profile APIs
```
| Method | Endpoint       | Description    |
| ------ | -------------- | -------------- |
| GET    | `/api/profile` | Get profile    |
| POST   | `/api/profile` | Create profile |
| PUT    | `/api/profile` | Update profile |
| DELETE | `/api/profile` | Delete profile |
```

## ⚙️ Installation & Setup

Follow these steps to run the project locally:<br>
- Prerequisites

    Make sure you have the following installed:

    - Java 17+
    - InteliJ IDE or Any Code Editor(Maven)
    - PostgreSQL
    - Git

- Open the project folder in Code Editor
- Clone the repository<br>
  git Clone: https://github.com/GOKULMS123/CareervexJobPortal.git
- Configure PostgreSQL database in application.properties
- Run the Spring Boot application

## 🌐 Access the Application or API Testing

You can test the APIs using **Postman** or **Swagger UI**.
###  🚀 Using Swagger UI (Recommended)

1. Start the application
2. Open:
   http://localhost:8080/swagger-ui/index.html
3. Explore available endpoints
4. Click **"Authorize"** and enter:

   Bearer YOUR_TOKEN

5. Execute APIs directly from the browser

### 📬 Using Postman

1. Import the provided Postman Collection
2. Set the base URL:

   http://localhost:8080

3. Register a new user:
   POST /api/auth/register

4. Login to get JWT token:
   POST /api/auth/login

5. Add token to headers:

   Authorization: Bearer YOUR_TOKEN

6. Test secured endpoints (Jobs, Applications, Profile)

## 🚧 Future Improvements
- Email notifications for job status updates
- Resume upload (file storage integration)
- Pagination & sorting for APIs
- Admin dashboard
- Real-time notifications (WebSockets)
- Docker deployment
- Microservices architecture

## 👨‍💻 Author

**Gokul Marimuthu**<br>
Aspiring Software Developer | Fresher<br>
[Email](gokulmarimuthu44@gmail.com) | [GitHub](https://github.com/GOKULMS123) | [LinkedIn](https://www.linkedin.com/in/gokul-marimuthu/)