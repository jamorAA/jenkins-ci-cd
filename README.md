# Demo Project

## 📌 Overview
CD - Deploy Application from Jenkins Pipeline to Compute Cloud Instance (automatically with docker)

---

## 🛠 Technologies Used
- Yandex Cloud
- Jenkins
- Docker
- Linux
- Git
- Java
- Maven
- Docker Hub

---

## 📖 Project Description
- Prepare Yandex Cloud Compute Cloud Instance for deployment (Install Docker)
- Create ssh key credentials for cloud machine server on Jenkins
- Extend the previous CI pipeline with deploy step to ssh into the remote Compute Cloud instance and deploy newly built image from Jenkins server
- Configure security group on Compute Cloud Instance to allow access to our web application

---

## 🌐 Live Demo
The Jenkins server is deployed using Yandex Cloud:

http://158.160.227.106:8080/

The application can be accessed on this address:

http://158.160.226.219:8080/

> ⚠️ Note: The addresses may be temporarily unavailable if the cloud server is inactive (for example, if the hosting service has not been paid).

---

## 📸 Screenshots
![cd part 1](screenshots/1.png)
![cd part 2](screenshots/2.png)
![cd part 3](screenshots/3.png)
