# 1. Java 기반 이미지 설정 (Eclipse Temurin JRE 17)
FROM --platform=linux/amd64 eclipse-temurin:17-jre

# 2. Python 설치 추가
RUN apt-get update && apt-get install -y python3 python3-pip python3-venv

# 3. 가상 환경 설정 및 활성화, Python 패키지 의존성 설치
COPY requirements.txt /app/requirements.txt
RUN python3 -m venv /app/venv && \
    /app/venv/bin/pip install --upgrade pip && \
    /app/venv/bin/pip install -r /app/requirements.txt

# 4. Spring Boot 애플리케이션 JAR 파일 복사
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} /app/app.jar

# 5. Python 스크립트 복사
COPY script.py /app/script.py

# 6. Spring Boot 애플리케이션 실행 (Python 가상 환경을 활성화한 상태에서 실행)
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
