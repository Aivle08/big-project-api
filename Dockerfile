FROM openjdk:17-slim

# 필수 유틸리티 설치
RUN apt-get update && apt-get install -y findutils

WORKDIR /app
COPY . .

# gradlew 실행 권한 부여
RUN chmod +x ./gradlew

# 애플리케이션 빌드
RUN ./gradlew build -x test

CMD ["java", "-jar", "build/libs/big-project-api-0.0.1-SNAPSHOT.jar"]
