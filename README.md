# 🎓 Campus Placement Portal

A full-stack web application developed using **Java Spring Boot**, **MySQL**, **Thymeleaf**, **Bootstrap**, and **JavaScript** to simplify the campus placement process.

The system allows students to search and apply for jobs, companies to manage job postings, and administrators to manage students, companies, jobs, and applications from a single platform.

---

# ✨ Features

### 👨‍🎓 Student Module
- Student Registration & Login
- Student Dashboard
- View & Update Profile
- Update Skills and CGPA
- View Available Jobs
- Apply for Jobs
- Track Application Status

### 👨‍💼 Admin Module
- Secure Admin Login
- Dashboard
- Manage Students
- Manage Companies
- Manage Jobs
- View Applications
- Complete CRUD Operations

### 🏢 Company Module
- Company Registration & Login
- Company Dashboard
- Post New Jobs
- View Posted Jobs
- View Applicants

### 🤖 AI Assistant
- Gemini AI Integration
- Placement-related Chat Assistant

### 📧 Email Support
- Gmail SMTP Integration
- Email Notification Support

---

# 🛠️ Tech Stack

## Backend
- Java 17
- Spring Boot
- Spring MVC
- Spring Data JPA
- Hibernate

## Frontend
- HTML5
- CSS3
- Bootstrap 5
- JavaScript
- Thymeleaf

## Database
- MySQL

## Tools
- Maven
- Git
- GitHub
- IntelliJ IDEA
- Railway

---

# 📁 Project Structure

```
PlacementPortal
│
├── src
│   ├── main
│   │   ├── java
│   │   │   ├── controller
│   │   │   ├── entity
│   │   │   ├── repository
│   │   │   ├── service
│   │   │   └── ai
│   │   │
│   │   ├── resources
│   │   │   ├── static
│   │   │   ├── templates
│   │   │   └── application.properties
│   │
│   └── test
│
├── pom.xml
└── README.md
```

---

# 💻 Prerequisites

Before running this project, make sure the following software is installed on your system.

- Java JDK 17 or later
- Apache Maven
- MySQL Server 8+
- Git
- IntelliJ IDEA (Recommended) or Eclipse
- Web Browser (Chrome, Edge, Firefox)

---

# ⚙️ Installation Guide

## Step 1 : Clone the Repository

```bash
git clone https://github.com/Riteshwagh45/placement-portal.git
```

Go inside the project folder.

```bash
cd placement-portal
```

---

## Step 2 : Create MySQL Database

Open MySQL Workbench and execute:

```sql
CREATE DATABASE placement_portal;
```

---

## Step 3 : Configure Database

Open

```
src/main/resources/application.properties
```

Update the database configuration.

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/placement_portal
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD
```

---

## Step 4 : Configure Gemini AI (Optional)

If you want to use the AI Assistant, add your Gemini API key.

```properties
gemini.api.key=YOUR_GEMINI_API_KEY
gemini.model=gemini-2.5-flash
```

If you don't configure the API key, the AI Assistant feature will not work, but the rest of the project will run normally.

---

## Step 5 : Configure Gmail (Optional)

To enable email notifications, configure your Gmail credentials.

```properties
spring.mail.username=YOUR_EMAIL
spring.mail.password=YOUR_APP_PASSWORD
```

---

## Step 6 : Install Dependencies

Open Terminal inside the project folder.

Run:

```bash
mvn clean install
```

---

## Step 7 : Run the Project

You can run the project using either:

### IntelliJ IDEA

Open the project and run

```
PlacementPortalApplication.java
```

OR

### Terminal

```bash
mvn spring-boot:run
```

---

## Step 8 : Open the Application

After the application starts successfully, open your browser.

```
http://localhost:8080
```

---

# ☁️ Railway Deployment

To deploy this project on Railway, configure the following environment variables.

```
DB_URL
DB_USERNAME
DB_PASSWORD

MAIL_USERNAME
MAIL_PASSWORD

GEMINI_API_KEY
GEMINI_MODEL
```

After setting the variables, connect the GitHub repository and deploy the project.

---

# 📚 What I Learned

During the development of this project, I gained hands-on experience in:

- Spring Boot Development
- MVC Architecture
- Spring Data JPA
- Hibernate ORM
- CRUD Operations
- MySQL Integration
- Authentication & Session Management
- Thymeleaf
- Bootstrap
- REST APIs
- Git & GitHub
- Railway Deployment
- Debugging and Problem Solving
---
# 👨‍💻 Developer

**Ritesh Wagh**

**M.Sc. Computer Science**

Nowrosjee Wadia College, Pune

GitHub:
https://github.com/Riteshwagh45

LinkedIn:
https://www.linkedin.com/in/ritesh-wagh-0b0302277

---

⭐ If you found this project helpful, consider giving it a **Star** on GitHub.
