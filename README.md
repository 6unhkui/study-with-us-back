> !!!!!!!!!! 작성중 !!!!!!!!!!

# 📚 Study With Us - Backend

## 🔍 Overview

![메인](https://user-images.githubusercontent.com/41765537/110615240-905e0f80-81d6-11eb-843f-60de4cbf6937.gif)
<br/>

Study With Us는 자기 개발을 위해 공부하는 사람들이 모여 공부하는 분야의 정보를 공유하고, 함께 의지를 다지기 위해 만든 커뮤니티 사이트입니다.<br/>
주요 기능은 아래와 같습니다.

-   회원가입, 로그인 & 소셜 로그인
-   스터디룸 생성 및 관리
-   출석 체크, 멤버 별 출석 현황 (그래프)
-   게시글, 댓글 & 대댓글 CRUD
-   채팅
-   새글 피드
-   스터디룸 찾기
-   마이페이지
-   다국어 지원 (영어, 한국어)

<!-- 자세한 기능 설명과 구현 내용은 [기술 블로그]()에 포스팅 해두었습니다. -->

## 💻 Dev Environment

-   OS : Mac OS, CentOS 7
-   Tool : Visual Studio Code, Git

## 🔧 Tech Stack
- Java 8
- Spring Boot 2.2
- Spring Security
- Spring Data JPA & Querydsl
- Build Tool : Gradle
- Embedded Server : Tomcat
- Database : Mysql, Redis
- Logging : Logback
- API Document : Swagger UI

## 🗄️ Database ERD
![erd](https://user-images.githubusercontent.com/41765537/110071340-3537b180-7dbf-11eb-8e78-3c70aff449bd.png)
- 이미지를 클릭하면 큰 이미지로 확인할 수 있습니다.

## 🗂 Directory Structure
```
.
├── java
│   ├── ...
│   │   ├── common
|   |   |   ├── annotaion
|   |   |   ├── config
|   |   |   ├── constant
|   |   |   ├── error
|   |   |   ├── properties
|   |   |   ├── pubsub
|   |   |   ├── security
|   |   |   └── util
│   │   ├── controller
│   │   │   └── ...
│   │   ├── domain
│   │   │   └── ...
│   │   ├── dto
│   │   │   └── ...
│   │   ├── repository
│   │   │   └── ...
│   │   ├── service
│   │   │   └── ...
│   |   └── StudyWithUsApplication.java
└── resources
    ├── config
    |   ├── local
    │   │   ├── application.yml
    │   │   └── application-auth.yml
    |   └── prod
    │       ├── application.yml
    │       └── application-auth.yml
    ├── db
    │   └── mysql
    |       └── schema.sql
    ├── i18n
    ├── static
    └── logback-spring.xml
```
