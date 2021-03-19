# 📚 Study With Us - Backend

## 🔍 Overview

![메인](https://user-images.githubusercontent.com/41765537/110615240-905e0f80-81d6-11eb-843f-60de4cbf6937.gif)
<br/>

Study With Us는 자기 개발을 위해 공부하는 사람들이 모여 정보를 공유하고, 함께 의지를 다지기 위해 만든 커뮤니티 사이트입니다.<br/><br/>
주요 기능은 아래와 같습니다.

- 회원가입, 로그인 & 소셜 로그인
- 스터디룸 생성 및 관리
- 출석 체크, 멤버 별 출석 현황 (그래프)
- 게시글, 댓글 & 대댓글 CRUD
- 채팅
- 새글 피드
- 스터디룸 찾기
- 마이페이지
- 다국어 지원 (영어, 한국어)

<br/>

## 💻 Dev Environment

- OS : Mac OS, CentOS 7
- Tool : IntelliJ, Git
  <br/><br/>

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
  <br/><br/>

## 🗄️ Database ERD

![erd](https://user-images.githubusercontent.com/41765537/111193812-ed562d00-85fd-11eb-89e3-cefbca409e7c.png)

- 이미지를 클릭하면 큰 이미지로 확인할 수 있습니다.
  <br/><br/>

## 🗂 Folder Structure

```
+ src +- main +- java
              |    +- {Project Package}
              |           ├── common
              |           |     ├── annotaion
              |           |     ├── constant
              |           |     ├── error
              |           |     ├── properties
              |           |     ├── pubsub
              |           |     ├── security
              |           |     └── util
              |           |
              |           ├── controller
              |           ├── domain
              |           ├── dto
              |           ├── repository
              |           ├── service
              |           └── StudyWithUsApplication.java
              +- resources
                     ├── config
                     |    ├── local
                     |    │    ├── application.yml
                     |    │    ├── application-auth.yml
                     |    │    └── application-database.yml
                     |    └── prod
                     |         ├── application.yml
                     |         ├── application-auth.yml
                     |         └── application-database.yml
                     ├── db
                     |    └── mysql
                     |         └── schema.sql
                     ├── i18n
                     └── logback-spring.xml

```

#### `common/annotaion`

커스텀 어노테이션을 모아둔 디렉토리입니다.

##### `@CurrentUser`

Spring Security로 인증된 사용자의 정보는 `@AuthenticationPrincipal`을 통해 Controller에서 주입받을 수 있습니다. 이때 주입되는 객체는 UserDetailsService의 loadUserByUsername 메서드의 리턴 값으로 UserDetails 인터페이스를 구현한 클래스의 인스턴스입니다. 이 클래스에서는 실제 사용자 정보만을 담은 객체를 멤버 변수로 가지고 있습니다.<br/>
Controller에서는 실제 사용자 정보만 사용하기 때문에 모든 Controller에서 매번 접근자로 멤버 변수에 접근하거나, 어노테이션에 expression을 기술해야 합니다. `@CurrentUser`은 이 과정을 처리하기 위해 만든 커스텀 어노테이션입니다.

#### `common/config`

외부 라이브러리를 Bean으로 등록하거나 자동 구성을 재정의 하는 등 설정 클래스를 모아둔 디렉토리입니다.

#### `common/constant`

애플리케이션 전역에서 사용하는 상수를 모아둔 클래스로, 인스턴스의 생성이 아니라 값의 제공만을 목적으로 하므로 모든 값은 static 변수로 이루어져 있습니다.

#### `common/error`

ErrorResponse DTO, Custom Exception, Exception Handler 클래스를 담고있습니다.<br/>
에러는 `@RestControllerAdvice` 어노테이션을 사용해 RestController 전역에서 Exception이 발생할 경우, 사용자에게 Exeption 별로 알맞은 에러 메시지를 응답해주도록 처리하였습니다.

#### `common/properties`

외부 프로퍼티 파일을 애플리케이션 내부로 주입받기 위한 프로퍼티 클래스를 모아둔 디렉토리입니다.

#### `common/pubsub`

Stomp 인메모리 브로커만으로도 채팅 메시지를 Pub/Sub 방식으로 주고 받을 수 있지만, 메시지 브로커가 해당 서버 인스턴스로 제한되기 때문에 클러스터 환경에서는 문제가 발생 할 수 있습니다. 확장성을 고려해 Redis에 채팅 데이터를 적재하고 외부 메시지 브로커로서 사용하도록 구성하였습니다.<br/>
이 디렉토리에는 클라이언트에게 전달받은 메시지를 Redis에 Publish하는 Publisher 클래스와 Redis Topic에 발행된 메시지를 채팅방을 구독하는 클라이언트에게 전달하는 Subscriber 클래스가 존재합니다.

#### `common/security`

Spring Security에서 사용하는 보안(인증, 인가)에 필요한 클래스를 모아둔 디렉토리입니다.<br/>
Filter, OAuth2, UserDetails 인터페이스의 구현체 등을 담고 있습니다.

#### `common/util`

유틸성 클래스를 모아둔 디렉토리입니다. File I/O, Image를 썸네일 크기로 리사이징&크롭 하는 등의 기능을 수행합니다.

#### `controller`

사용자의 요청과 응답을 처리하는 Controller 클래스를 모아둔 디렉토리입니다.

#### `domain`

실제 데이터베이스 테이블과 매칭되는 Entity 클래스를 모아둔 디렉토리입니다.

#### `dto`

API 스펙에 맞춰 통신을 위해 사용하는 DTO 클래스를 모아둔 디렉토리입니다. <br/>
도메인 별로 하나의 외부 클래스를 생성하고, DTO는 그 안의 Static Inner Class로 구현하였습니다.

#### `repository`

JPA를 사용해 데이터베이스에 직접 접근해 데이터를 처리하는 Repository 클래스를 모아둔 디렉토리입니다.

#### `service`

비즈니스 로직 수행하는 Service 클래스를 모아둔 디렉토리입니다.<br/>

#### `resources/config/**`

서버 환경을 로컬과 운영으로 두고 설정 파일을 분리하였습니다.<br/>
커맨드 라인에서 gradle로 서버를 실행하거나 빌드 할때 인자로 profile을 값을 전달하고 그 값에 맞는 설정 파일을 읽어오도록 세팅하였습니다.

#### `resources/db/{DB_NAME}/schema.sql`

운영 환경에서 애플리케이션 기동 시 데이터베이스의 스키마 생성을 위해 동작하는 DDL 스크립트입니다. <br/>
설정 파일의 값이 `spring.datasource.initialization-mode=always`일 때 실행되며, 최초 1회 서버 세팅이 정상적으로 수행 된 후에는 DDL 스크립트가 실행되지 않도록 `never`로 설정 값을 변경해줍니다.

- JPA에서 스키마 자동 생성을 지원하지만, 운영 환경에서는 사용하지 않습니다.

#### `resources/i18n`

응답 값의 다국어 처리를 위한 언어 설정 파일이 담긴 디렉토리입니다. HTTP Request시 전달되는 Locale 정보에 따라 사용자 Locale 값을 변경하는 LocaleChangeInterceptor를 추가해주었고, MessageSource가 설정된 Locale 값에 맞는 언어 설정 파일의 메시지를 응답해주는 방식으로 동작합니다. 다국어 파일은 `{basename}_{언어코드}.yml` 명명 규칙을 따르며, 한국어와 영어를 지원합니다.

#### `resourcs/logback-spring.xml`

로그를 출력하기 위한 Logback 설정 파일입니다.
