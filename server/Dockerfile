FROM openjdk:21 as stage1

WORKDIR /server

# gradle 빌드시에 필요한 파일들을 workdir로 copy
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src

# Gradle Wrapper 스크립트에 실행 권한을 부여
RUN chmod +x ./gradlew
RUN ./gradlew bootJar

FROM openjdk:21

WORKDIR /server

# 빌드된 JAR 파일을 복사
COPY --from=stage1 /server/build/libs/*.jar server.jar

# 애플리케이션 실행
ENTRYPOINT [ "java", "-jar", "server.jar" ]
