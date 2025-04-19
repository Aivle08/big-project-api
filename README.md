# big-project-api

## 프로젝트 설명

이 프로젝트는 고급 채용 플랫폼을 위한 백엔드 API입니다. 채용 공고, 지원자, 평가 등을 관리하며, 이력서 분석 및 지원자 점수 책정과 같은 작업을 위해 AI 서비스를 활용합니다.

## 아키텍처
![image](https://github.com/user-attachments/assets/e46098ec-18c7-4d69-a9a4-d33ca23238c0)

## 기술 스택

*   **언어:** Java 17
*   **프레임워크:** Spring Boot 3.4.1
*   **데이터:** Spring Data JPA
*   **데이터베이스:** MySQL (운영), H2 (개발/테스트)
*   **보안:** Spring Security, JWT (JSON Web Tokens)
*   **API 문서화:** Swagger (Springdoc OpenAPI)
*   **파일 저장소:** AWS S3
*   **비동기 HTTP:** Spring WebFlux (WebClient)
*   **이메일:** Spring Boot Mail Starter
*   **빌드 도구:** Gradle
*   **유틸리티:** Lombok

## 주요 기능

*   **사용자 관리:** JWT를 이용한 사용자 회원가입 및 인증.
*   **채용 공고 관리:** 채용 공고 생성, 수정, 삭제 및 조회.
*   **지원자 관리:** 지원서 제출 관리 (이력서 S3 업로드 포함).
*   **AI 연동:**
    *   처리를 위한 외부 AI API와의 통신 (WebClient 사용).
    *   가능한 기능: 이력서 정보 추출, 자동 점수 채점, 질문 생성 등.
*   **평가 시스템:** 평가 기준 정의 및 지원자 점수 채점.
*   **댓글 기능:** 지원자 또는 평가에 대한 댓글 추가.
*   **이력서 검색/관리:** 분석된 이력서 데이터 검색 또는 관리 관련 서비스 (추정).
*   **API 문서화:** Swagger UI를 통한 대화형 API 문서 제공.

## 주요 엔티티 (Models)

*   **Applicant:** 지원자 정보
*   **Comment:** 댓글 정보
*   **Company:** 회사 정보
*   **Department:** 부서 정보
*   **Evaluation:** 평가 정보 (전반적인 평가)
*   **EvaluationDetail:** 평가 상세 항목 정보
*   **EvaluationScore:** 평가 점수 정보
*   **Post:** 게시물 정보 (공지사항 등 다른 게시판일 수 있음)
*   **Recruitment:** 채용 공고 정보
*   **ResumeRetriever:** 이력서 검색/처리 관련 정보 (추정)
*   **Users:** 사용자(회원) 정보
*   **UsersDetails:** Spring Security 사용자 상세 정보 (인증/인가용)

## 설정 및 실행 방법

1.  **저장소 복제:**
    ```bash
    git clone <repository-url>
    cd big-project-api
    ```
2.  **데이터베이스 설정:** `src/main/resources/application.properties` (또는 `application-prod.properties` 등 프로필별 파일)에서 데이터베이스 연결 정보를 업데이트합니다.
3.  **AWS S3 설정:** `src/main/resources/application.properties` 또는 환경 변수/AWS 설정을 통해 AWS 자격 증명 및 S3 버킷 정보를 설정합니다.
4.  **이메일 설정:** `src/main/resources/application.properties`에서 이메일 서버 정보를 설정합니다.
5.  **프로젝트 빌드:**
    ```bash
    ./gradlew build
    ```
6.  **애플리케이션 실행:**
    ```bash
    ./gradlew bootRun
    ```
    또는 `build/libs` 디렉토리에 생성된 JAR 파일을 실행합니다:
    ```bash
    java -jar build/libs/big-project-api-0.0.1-SNAPSHOT.jar
    ```

## API 문서

애플리케이션 실행 후, 일반적으로 다음 주소에서 Swagger UI를 사용할 수 있습니다:
`http://localhost:8080/api-docs` (기본 포트 8080 및 컨텍스트 경로 가정)

이 인터페이스를 통해 사용 가능한 API 엔드포인트를 탐색하고 테스트할 수 있습니다.
