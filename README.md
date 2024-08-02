# 제작 취지
Docker를 통한 Zookeeper 앙상블 구현 및 Spring Kafka를 활용한 Kafka 구현 예시 제작.

아직 Kafka를 경험해보지 못한 분들을 위해 간단한 예시를 제작하였습니다.

관련 문서는 다음 두 블로그를 참고해주세요.

1. [Kafka 개념](https://jd6186.github.io/KafkaBasic/)
2. [Spring Kafka를 통한 활용 방법]()

# 개발 환경
- Spring Kafka
- gradle
- redis
- mysql
- docker

# 실행 방법
1. docker-compose.yml 파일을 이용하여 테스트용 redis, mysql 데이터베이스를 실행합니다.

    ```shell
    docker-compose -f docker-compose-database.yml up -d
    ```

2. src/main/resources/sql 폴더에 있는 sql 파일을 이용하여 테이블 및 테스트용 데이터를 생성합니다.

3. docker-compose.yml 파일을 이용하여 Zookeeper 앙상블을 구현합니다.

    ```shell
    docker-compose up -d
    ```

4. IntelliJ IDEA를 통해 Spring Kafka 프로젝트를 실행합니다.

5. 사용하시는 API 테스트 툴을 통해 API를 호출합니다.