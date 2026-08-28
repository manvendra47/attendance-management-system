# Attendance Management System

A Spring Boot web application for managing student/employee attendance, built with MVC architecture, MySQL, Thymeleaf, and Spring Security.

## Tech Stack
- Java 17, Spring Boot 3.3
- Spring MVC, Spring Data JPA (Hibernate)
- MySQL
- Thymeleaf (+ thymeleaf-extras-springsecurity6)
- Spring Security (form login, role-based access, BCrypt passwords)

## Features
- **Role-based access**: `ADMIN`, `FACULTY`, `STUDENT` — enforced by Spring Security and separate URL namespaces (`/admin/**`, `/user/**`).
- **Admin module**: create/edit/delete users, assign roles, mark attendance for any user, edit/delete any attendance record, dashboard with summary stats.
- **User module** (faculty/student): dashboard with personal attendance summary (present/absent/leave counts), full attendance history table.
- Passwords are BCrypt-hashed; a `CommandLineRunner` seeds a default admin + two demo accounts on first run.

## Project Structure
```
src/main/java/com/attendance/app/
  config/       SecurityConfig, DataInitializer
  model/        User, Attendance, Role, AttendanceStatus
  repository/   UserRepository, AttendanceRepository (Spring Data JPA)
  service/      UserService, AttendanceService, CustomUserDetailsService
  controller/   HomeController, AdminUserController, AdminAttendanceController, UserAttendanceController
src/main/resources/
  application.properties
  templates/    login.html, admin/*.html, user/*.html, fragments/navbar.html
  static/css/style.css
```

## Setup

### 1. Create the MySQL database
```sql
CREATE DATABASE attendance_db;
```
(The datasource URL also has `createDatabaseIfNotExist=true`, so this step is optional if your MySQL user has permission to create databases.)

### 2. Configure credentials
Edit `src/main/resources/application.properties` and set your MySQL username/password:
```properties
spring.datasource.username=root
spring.datasource.password=root
```

### 3. Run the application
```bash
mvn spring-boot:run
```
Hibernate will auto-create the `users` and `attendance` tables (`ddl-auto=update`).

The app starts at **http://localhost:8080**.

### 4. Log in
Default seeded accounts (created automatically on first run):

| Role    | Username | Password    |
|---------|----------|-------------|
| Admin   | admin    | admin123    |
| Faculty | asmith   | faculty123  |
| Student | jdoe     | student123  |

Admins land on `/admin/dashboard`; faculty/students land on `/user/dashboard`.

## Notes / Next Steps
- This is a beginner-scope reference implementation — for production use, move credentials out of `application.properties` into environment variables, add pagination to the record tables, add bulk/date-range attendance marking, and add server-side validation messages per field.
- The `Attendance` table has a unique constraint on `(user_id, attendance_date)`; re-marking attendance for the same user/date updates the existing record instead of creating a duplicate.
