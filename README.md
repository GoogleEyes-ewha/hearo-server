<img src="https://github.com/GoogleEyes-ewha/hearo-server/assets/94354545/04da9496-985d-457b-937d-5cd9f5f7b654" alt="hearo-banner" border="0" width="1000px" />

# 2024 Solution Challenge: Hearo 🦸🏻‍♀️

> GDSC EWHA - Team `GoogleEyes`
>
> Minju Choi(@hmuri), Cherry Yi(@official-Cherry), Youngseo Lim(@yslim21), Haewon Lee(@Haewonny)

## 💡 Project Introduction

**Hearo** is a web-based, voice-driven online shopping service designed specifically for individuals with visual impairments. <br>
Its primary goal is to serve as an **eye** for the blind. 
Hearo uses **Google AI** to provide oral descriptions of product nutrition fact images and to summarize product reviews. <br>
This initiative aims to **improve the overall shopping experience** for individuals with visual impairments.


## 🎯 Hearo's Goal
<p align="center">
<img src="https://github.com/GoogleEyes-ewha/hearo-server/assets/94354545/18b604ef-7cee-4c45-b7c4-3789dfafa6d4" border="0" width="800px">
</p>

## 🛠️ Tech Stack
- Java 17
- Spring boot 3
  - Spring Web MVC, Spring Security
  - Spring Data JPA
- Docker
- Google Cloud Platform (GCP)
  - VM Instance
  - Cloud SQL, Cloud Storage
  - Document AI
  - Speech-to-Text (STT), Text-to-Speech (TTS)
- Gemini Pro

## ☁️ How to run
1. Clone project
  ```bash
  $ git clone https://github.com/GoogleEyes-ewha/hearo-server.git
  ```
     
2. Set `application.properties`
```yaml
# Database
SPRING_DATABASE_URL=jdbc:mysql://[Cloud SQL Public IP]/[Database Name]?serverTimezone=UTC&characterEncoding=UTF-8
SPRING_DATASOURCE_PASSWORD=(YOUR_PASSWORD)

# JWT - You can customize the expiration time
JWT_TOKEN_EXPIRATION_TIME=7200000
JWT_REFRESH_TOKEN_EXPIRATION_TIME=604800000
JWT_SECRET_KEY=(YOUR_SECRET_KEY)

# Gemini
GEMINI_URL=https://generativelanguage.googleapis.com/v1/models/gemini-pro:generateContent
GEMINI_KEY=(YOUR_API_KEY)

# OAuth2
GOOGLE_CLIENT_ID=(YOUR_CLIENT_ID)
GOOGLE_CLIENT_SECRET=(YOUR_CLIENT_SECRET)
GOOGLE_REDIRECT_URL=(YOUR_REDIRECT_URL)

# SSL
KEY_STORE_PASSWORD=(YOUR_PASSWORD)

# Document AI
DOCUMENT_PROJECT_ID=(YOUR_PROJECT_ID)
DOCUMENT_PARSER=FORM_PARSER_PROCESSOR
```
3. Run `HearoApplication.java`
   

## 📁 API Documents
<p align="center">
<img src="https://github.com/GoogleEyes-ewha/hearo-server/assets/94354545/d0008b02-daad-4a85-b36e-6ec33d46e2cb" width="500"/>
</p>

## 🔍 Architecture
<p align="center">
<img src="https://github.com/GoogleEyes-ewha/hearo-server/assets/94354545/6517e268-67b1-4382-a84f-8fba58439157" width="800"/>
</p>

## 👩🏻‍💻 Back-End Contributors

| Youngseo Lim                    | Haewon Lee                          | 
| --------------------------------- | --------------------------------- | 
| ![](https://github.com/yslim21.png) | ![](https://github.com/haewonny.png) | 
| <p align="center"><a href="https://github.com/yslim21">@yslim21</a></p> | <p align="center"><a href="https://github.com/haewonny">@Haewonny</a></p> | 

<img src="https://github.com/GoogleEyes-ewha/hearo-server/assets/94354545/54df3e3e-2eb3-47f7-8f69-6914139fc152" border="0" width="1000px" />
