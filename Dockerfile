# Java 21 런타임 사용
FROM openjdk:21-jdk-slim

# 작업 디렉토리 설정
WORKDIR /app

# 빌드 결과물(jar)을 컨테이너 내부로 복사
COPY build/libs/*SNAPSHOT.jar app.jar

# 컨테이너 실행 시 Spring Boot 실행
ENTRYPOINT ["java", "-jar", "app.jar"]